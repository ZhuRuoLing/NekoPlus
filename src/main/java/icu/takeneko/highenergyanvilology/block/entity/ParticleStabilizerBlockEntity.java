package icu.takeneko.highenergyanvilology.block.entity;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.highenergyanvilology.block.entity.logic.stabilizer.ParticleStabilizerLogic;
import icu.takeneko.highenergyanvilology.block.entity.logic.stabilizer.ParticleStabilizerLogicHost;
import icu.takeneko.highenergyanvilology.foundation.block.entity.BlockCollisionEventReceiver;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HEOverclockablePowerConsumer;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HESynedBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.entity.Overclockable;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandler;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandlerSlice;
import icu.takeneko.highenergyanvilology.foundation.inventory.ItemHandlerOwner;
import icu.takeneko.highenergyanvilology.foundation.ui.HEBlockEntityUIHolder;
import icu.takeneko.highenergyanvilology.recipes.AirCondensingRecipe;
import icu.takeneko.highenergyanvilology.ui.ParticleStabilizerUI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ParticleStabilizerBlockEntity
    extends HESynedBlockEntity
    implements HEOverclockablePowerConsumer, HEBlockEntityUIHolder, Tickable, BlockCollisionEventReceiver, ItemHandlerOwner, Overclockable, ParticleStabilizerLogicHost {

    public static final int MACHINE_COOLDOWN = 30 * 20;

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ParticleStabilizerBlockEntity.class);

    @Persisted
    @Getter
    private final HEItemHandler itemHandler = new HEItemHandler(5, this);

    @Getter
    @Setter
    private PowerGrid grid;

    @Persisted
    @DescSynced
    @Getter
    private State state = State.COOLING;

    @Persisted
    @Getter
    private int countdown = MACHINE_COOLDOWN;

    @DescSynced
    @Getter
    @Setter
    private boolean isOverload = false;

    @Setter
    private int efficiency = 1;

    @Getter
    @Setter
    @DescSynced
    private int progress;

    @Getter
    @Setter
    @Nullable
    private AirCondensingRecipe currentRecipe;

    private final ParticleStabilizerLogic logic = new ParticleStabilizerLogic.Impl();

    public ParticleStabilizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void gridTick() {
    }

    @Override
    public void tick() {
        flushState(level, getBlockPos());
        if (this.isOverload) return;
        if (countdown > 0) {
            this.countdown = Math.max(countdown - efficiency, 0);
            updateState(State.COOLING);
        } else {
            updateState(State.WORKING);
        }
        logic.tick(this);
    }

    private void updateState(State value) {
        if (this.state != value) {
            this.state = value;
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return level;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public boolean acceptCollision(FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event) {
        return logic.handleCollision(this, entity, speed, event);
    }

    @Override
    public void onContentChanged() {
        setChanged();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 0) {
            return logic.isValidTriggerItem(stack);
        }
        return true;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(new ParticleStabilizerUI(this), this, entityPlayer);
    }

    @Override
    public int getBaseOverclockCost() {
        return 32;
    }

    @Override
    public int maxOverclockRatio() {
        return 100;
    }

    @Override
    public boolean isOverclockable() {
        return this.state == State.COOLING;
    }

    @Override
    public int getBaseInputPower() {
        return 16;
    }

    @Override
    public int getOverclockedInputPower() {
        return 32 * efficiency;
    }

    @Override
    public ItemStack getTriggerItem() {
        return itemHandler.getStackInSlot(0);
    }

    @Override
    public ItemStack tryConsumeTriggerItem() {
        return itemHandler.slice(0, 1).extractItem(0, 1, false);
    }

    @Override
    public HEItemHandlerSlice getOutputItemHandler() {
        return itemHandler.slice(1, 5);
    }

    @Override
    public boolean hasValidWorkingState() {
        return !this.isOverload && this.state == ParticleStabilizerBlockEntity.State.WORKING;
    }

    @Override
    public void resetCooldown() {
        this.countdown = ParticleStabilizerBlockEntity.MACHINE_COOLDOWN;
    }

    @Override
    public void resetState() {
        this.state = ParticleStabilizerBlockEntity.State.COOLING;
    }

    public enum State {
        COOLING, WORKING
    }
}

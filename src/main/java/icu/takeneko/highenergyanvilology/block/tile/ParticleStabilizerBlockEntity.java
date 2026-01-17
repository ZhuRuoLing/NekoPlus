package icu.takeneko.highenergyanvilology.block.tile;

import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.highenergyanvilology.block.tile.logic.stabilizer.ParticleStabilizerLogic;
import icu.takeneko.highenergyanvilology.block.tile.logic.stabilizer.ParticleStabilizerLogicHost;
import icu.takeneko.highenergyanvilology.foundation.block.tile.BlockCollisionEventReceiver;
import icu.takeneko.highenergyanvilology.foundation.block.tile.HEOverclockablePowerConsumer;
import icu.takeneko.highenergyanvilology.foundation.block.tile.HESynedBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.tile.Overclockable;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandler;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandlerSlice;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandlerOwner;
import icu.takeneko.highenergyanvilology.recipe.AirCondensingRecipe;
import icu.takeneko.highenergyanvilology.ui.ParticleStabilizerUI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ParticleStabilizerBlockEntity
    extends HESynedBlockEntity
    implements HEOverclockablePowerConsumer, IModularUIHolder, Tickable, BlockCollisionEventReceiver, HEItemHandlerOwner, Overclockable, ParticleStabilizerLogicHost {

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
    @Persisted
    private int progress;

    @Getter
    @Setter
    @DescSynced
    private int maxProgress;

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
        flushState();
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

    @Override
    public @Nullable ModularUI getModularUI() {
        return new ModularUI(UI.of(new ParticleStabilizerUI(this)));
    }

    public enum State {
        COOLING, WORKING
    }
}

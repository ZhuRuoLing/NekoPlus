package icu.takeneko.highenergyanvilology.block.entity;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.highenergyanvilology.foundation.block.entity.BlockCollisionEventReceiver;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HEPowerConsumer;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HESynedBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.entity.Tickable;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandler;
import icu.takeneko.highenergyanvilology.foundation.inventory.ItemHandlerOwner;
import icu.takeneko.highenergyanvilology.foundation.ui.HEBlockEntityUIHolder;
import icu.takeneko.highenergyanvilology.ui.ParticleStabilizerUI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
    implements HEPowerConsumer, HEBlockEntityUIHolder, Tickable, BlockCollisionEventReceiver, ItemHandlerOwner {

    public static final int MACHINE_COOLDOWN = 30 * 20;

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ParticleStabilizerBlockEntity.class);

    @Persisted
    @Getter
    private final HEItemHandler itemHandler = new HEItemHandler(2, this);

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

    public ParticleStabilizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void tick() {
        flushState(level, getBlockPos());
        if (this.isOverload) return;
        if (countdown > 0) {
            this.countdown--;
            updateState(State.COOLING);
        } else {
            updateState(State.WORKING);
        }
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
    public int getInputPower() {
        return state == State.COOLING ? 32 : 16;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public boolean acceptCollision(FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event) {
        if (speed >= 32 && !this.isOverload && this.state == State.WORKING) {
            event.getLevel().playSound(
                null,
                this.getBlockPos(),
                SoundEvents.ANVIL_LAND,
                SoundSource.BLOCKS,
                1.2f,
                1.2f
            );
            entity.discard();
            processStabilize(entity.getBlockState().getBlock());
        } else {
            event.getLevel().playSound(
                null,
                this.getBlockPos(),
                SoundEvents.ANVIL_DESTROY,
                SoundSource.BLOCKS,
                2f,
                0.8f
            );
            event.getLevel().explode(
                null,
                this.getBlockPos().getX(),
                this.getBlockPos().getY(),
                this.getBlockPos().getZ(),
                5,
                Level.ExplosionInteraction.NONE
            );
            if (event.getLevel().getRandom().nextDouble() > 0.5) {
                entity.discard();
            }
        }
        this.countdown = MACHINE_COOLDOWN;
        this.state = State.COOLING;
        event.setAnvilDamage(true);
        return true;
    }

    //ldlib issue
    private void processStabilize(net.minecraft.world.level.block.Block anvil) {
        itemHandler.setStackInSlot(1, anvil.asItem().getDefaultInstance());
    }

    @Override
    public void onContentChanged() {
        setChanged();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(new ParticleStabilizerUI(this), this, entityPlayer);
    }

    public enum State {
        COOLING, WORKING
    }
}

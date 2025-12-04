package icu.takeneko.highenergyanvilology.block.entity;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HEPowerConsumer;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HESynedBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.entity.Tickable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class ParticleStabilizerBlockEntity
    extends HESynedBlockEntity
    implements HEPowerConsumer, Tickable {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ParticleStabilizerBlockEntity.class);

    @Getter
    @Setter
    private PowerGrid grid;

    @Persisted
    @DescSynced
    @Getter
    private State state = State.COOLING;

    @Persisted
    @DescSynced
    private int countdown = 30 * 20;

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

    public enum State {
        COOLING, WORKING;
    }
}

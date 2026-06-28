package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.block.BatteryBlock;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPPowerProducer;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BatteryBlockEntity extends NPSynedBlockEntity implements NPPowerProducer, Tickable {
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(BatteryBlockEntity.class);

    @Nullable
    @Getter
    @Setter
    private PowerGrid grid;

    @Persisted
    private int maxDischargingRate = 100;

    @Persisted
    private long storedPower = 0;

    @Persisted
    private boolean discharging = false;

    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void gridTick() {
    }

    @Override
    public void tick() {
        flushState();
        level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos()).setValue(BatteryBlock.DISCHARGING, discharging));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public int getOutputPower() {
        return 0;
    }

    @Override
    public void setOverload(boolean value) {
        level.setBlockAndUpdate(getPos(), getBlockState().setValue(BatteryBlock.OVERLOAD, value));
    }

    @Override
    public boolean isOverload() {
        return level.getBlockState(getPos()).getValue(BatteryBlock.OVERLOAD);
    }
}

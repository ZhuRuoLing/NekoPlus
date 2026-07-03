package icu.takeneko.nekoplus.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HugeBatteryBlockEntity extends BatteryBlockEntity {
    public HugeBatteryBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        super(type, pos, blockState);
    }

    @Override
    public long getCapacity() {
        return super.getCapacity() * 27;
    }

    @Override
    public int getMaxChargingRateLimit() {
        return super.getMaxChargingRateLimit() * 27;
    }

    @Override
    public int getDefaultMaxDischargingRate() {
        return super.getDefaultMaxDischargingRate() * 27;
    }

    @Override
    public int getMaxDischargingRateLimit() {
        return super.getMaxDischargingRateLimit() * 27;
    }
}

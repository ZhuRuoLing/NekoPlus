package icu.takeneko.nekoplus.block.tile;

import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.internal.StampingPlatformsInternals;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@Setter
public class StampingPlatformBlockEntity extends BlockEntity implements Tickable {
    private boolean isLaserTarget;
    @Nullable
    private BlockPos laserEmitterPosition;

    public StampingPlatformBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void tick() {
        if (laserEmitterPosition == null || !isLaserTarget) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StampingPlatformsInternals.LASER_TARGETED, false));
            return;
        }
        if (level.getBlockEntity(laserEmitterPosition) instanceof BaseLaserBlockEntity heLaserBE) {
            if (!heLaserBE.getBlockPos().equals(laserEmitterPosition) || heLaserBE.getLaserLevel() < 64) {
                isLaserTarget = false;
                laserEmitterPosition = null;
            }
        } else {
            isLaserTarget = false;
            laserEmitterPosition = null;
        }
        if (getBlockState().getValue(StampingPlatformsInternals.LASER_TARGETED) != isLaserTarget) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StampingPlatformsInternals.LASER_TARGETED, isLaserTarget));
        }
    }
}

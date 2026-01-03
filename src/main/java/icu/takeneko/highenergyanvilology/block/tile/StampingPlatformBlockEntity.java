package icu.takeneko.highenergyanvilology.block.tile;

import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.internal.StampingPlatformsInternal;
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
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StampingPlatformsInternal.LASER_TARGETED, false));
            return;
        }
        if (level.getBlockEntity(laserEmitterPosition) instanceof HighEnergyLaserBlockEntity heLaserBE) {
            if (!heLaserBE.getPos().equals(laserEmitterPosition)) {
                isLaserTarget = false;
                laserEmitterPosition = null;
            }
        } else {
            isLaserTarget = false;
            laserEmitterPosition = null;
        }
        if (getBlockState().getValue(StampingPlatformsInternal.LASER_TARGETED) != isLaserTarget) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StampingPlatformsInternal.LASER_TARGETED, isLaserTarget));
        }
    }
}

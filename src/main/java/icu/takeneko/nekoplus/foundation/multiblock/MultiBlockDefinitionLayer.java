package icu.takeneko.nekoplus.foundation.multiblock;

import icu.takeneko.nekoplus.foundation.multiblock.prediction.BlockStatePrediction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MultiBlockDefinitionLayer implements MultiBlockMatcher {

    private final BlockStatePrediction[][] predictionLayer;
    private final int sizeX;
    private final int sizeZ;

    public MultiBlockDefinitionLayer(
        BlockStatePrediction[][] predictionLayer,
        int sizeX,
        int sizeZ
    ) {
        this.predictionLayer = predictionLayer;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
    }

    @Override
    public boolean matches(BlockPos start, Level level, Direction structureFacing) {
        for (int x = sizeX; x > 0; x--) {
            for (int z = sizeZ; z > 0; z--) {
                BlockPos pos = start.offset(x, 0, z);
                BlockStatePrediction prediction = predictionLayer[x][z];
                BlockState blockState = level.getBlockState(pos);
                if (!prediction.test(blockState)) {
                    return false;
                }
            }
        }
        return true;
    }
}

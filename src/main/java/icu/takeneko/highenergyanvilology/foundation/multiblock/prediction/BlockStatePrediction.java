package icu.takeneko.highenergyanvilology.foundation.multiblock.prediction;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public interface BlockStatePrediction extends Predicate<BlockState> {
    static BlockStatePrediction isBlock(Block block) {
        return s -> s.is(block);
    }
}

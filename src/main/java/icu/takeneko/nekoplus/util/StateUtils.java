package icu.takeneko.nekoplus.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class StateUtils {
    public static boolean always(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }
}

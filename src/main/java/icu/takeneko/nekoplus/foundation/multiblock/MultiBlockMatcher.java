package icu.takeneko.nekoplus.foundation.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface MultiBlockMatcher {

    boolean matches(BlockPos start, Level level, Direction structureFacing);
}

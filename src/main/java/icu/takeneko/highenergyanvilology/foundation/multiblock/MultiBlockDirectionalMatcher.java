package icu.takeneko.highenergyanvilology.foundation.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.Map;

public class MultiBlockDirectionalMatcher implements MultiBlockMatcher {
    private final Map<Direction, MultiBlockMatcher> matcherEachDirection;

    public MultiBlockDirectionalMatcher(Map<Direction, MultiBlockMatcher> matcherEachDirection) {
        this.matcherEachDirection = matcherEachDirection;
    }

    @Override
    public boolean matches(BlockPos start, Level level, Direction structureFacing) {
        MultiBlockMatcher matcher = matcherEachDirection.get(structureFacing);
        if (matcher == null) {
            return false;
        }

        return matcher.matches(start, level, structureFacing);
    }
}

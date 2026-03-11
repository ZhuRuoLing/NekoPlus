package icu.takeneko.nekoplus.foundation.multiblock;

import icu.takeneko.nekoplus.foundation.multiblock.builder.MultiBlockDefinitionBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.List;

public class MultiBlockDefinition implements MultiBlockMatcher {

    private final List<MultiBlockDefinitionLayer> definitionEachFacing;
    private final int height;

    public MultiBlockDefinition(List<MultiBlockDefinitionLayer> definitionEachFacing, int height) {
        this.definitionEachFacing = definitionEachFacing;
        this.height = height;
    }

    public static MultiBlockDefinitionBuilder builder() {
        return new MultiBlockDefinitionBuilder();
    }

    @Override
    public boolean matches(BlockPos start, Level level, Direction structureFacing) {
        for (int i = height; i > 0; i--) {
            BlockPos pos = start.below(i);
            MultiBlockDefinitionLayer layer = definitionEachFacing.get(i);
            if (!layer.matches(pos, level, structureFacing)) {
                return false;
            }
        }
        return true;
    }
}

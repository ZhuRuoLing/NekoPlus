package icu.takeneko.nekoplus.internal;

import icu.takeneko.nekoplus.all.NPBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class HeatableBlockEntityInternals {

    public interface Extension {
        void setInsulationCount(int count);

        default void updateInsulation(Level level, BlockPos pos) {
            int count = 0;
            for (Direction direction : Direction.values()) {
                if (level.getBlockState(pos.relative(direction)).is(NPBlocks.FOUNDRY_SAND.get())) {
                    count++;
                    if (count == 4) break;
                }
            }
            this.setInsulationCount(count);
        }
    }
}

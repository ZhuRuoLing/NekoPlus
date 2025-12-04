package icu.takeneko.highenergyanvilology.util;

import icu.takeneko.highenergyanvilology.foundation.block.entity.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityUtil {

    public static <T extends BlockEntity & Tickable> BlockEntityTicker<T> createTicker() {
        return new BlockEntityTickerImpl<>();
    }

    private static class BlockEntityTickerImpl<T extends BlockEntity & Tickable> implements BlockEntityTicker<T> {

        @Override
        public void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
            blockEntity.tick();
        }
    }
}

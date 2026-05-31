package icu.takeneko.nekoplus.foundation.block.tile.hatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface HatchLogicHost {
    BlockPos getBlockPos();

    @Nullable
    Level getLevel();

    BlockState getBlockState();

    void markDirty();
}

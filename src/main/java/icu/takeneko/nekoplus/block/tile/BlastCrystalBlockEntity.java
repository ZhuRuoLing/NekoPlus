package icu.takeneko.nekoplus.block.tile;

import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlastCrystalBlockEntity extends NPSynedBlockEntity {

    public BlastCrystalBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        super(type, pos, blockState);
    }
}

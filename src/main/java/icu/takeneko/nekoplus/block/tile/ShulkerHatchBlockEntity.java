package icu.takeneko.nekoplus.block.tile;

import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ShulkerHatchBlockEntity extends NPSynedBlockEntity {
    public ShulkerHatchBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        super(type, pos, blockState);
    }

    public void eject(Player player) {

    }

    public void insert(Player player) {

    }
}

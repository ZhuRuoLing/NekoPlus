package icu.takeneko.nekoplus.block.tile;

import icu.takeneko.nekoplus.block.ShulkerHatchBlock;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class ShulkerHatchBlockEntity extends NPSynedBlockEntity {
    public ShulkerHatchBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        super(type, pos, blockState);
    }

    public void eject(Player player) {
        ResourceHandler<ItemResource> handler = getHandler();
        if (handler.getAmountAsLong(0) <= 0) return;

    }

    private ResourceHandler<ItemResource> getHandler() {
        Direction facing = getBlockState().getValue(ShulkerHatchBlock.FACING);
        return level.getCapability(
            Capabilities.Item.BLOCK,
            getBlockPos().relative(facing),
            facing.getOpposite()
        );
    }

    public void insert(Player player) {

    }
}

package icu.takeneko.highenergyanvilology.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TardisBlockEntity extends BlockEntity {
    public TardisBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    private boolean animation = false;

    public void onClick() {
        animation = !animation;
    }
}

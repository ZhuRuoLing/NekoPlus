package icu.takeneko.nekoplus.foundation.block;

import dev.dubhe.anvilcraft.block.multipart.AbstractMultiPartBlock;
import dev.dubhe.anvilcraft.block.state.ISimpleMultiPartBlockState;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class NPSimpleMultiPartBlock<P extends Enum<P> & ISimpleMultiPartBlockState<P>> extends AbstractMultiPartBlock<P> implements NPUIBlock {

    public NPSimpleMultiPartBlock(Properties properties) {
        super(properties);
    }

    public boolean hasEnoughSpace(BlockPos pos, LevelReader level) {
        for (P part : getParts()) {
            BlockPos pos1 = pos.offset(part.getOffset());
            if (level.isOutsideBuildHeight(pos1)) return false;
            BlockState state = level.getBlockState(pos1);
            if (!state.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    public Vec3i getMainPartOffset() {
        return new Vec3i(0, 0, 0);
    }

    @Override
    public Vec3i offsetFrom(BlockState state, P part) {
        return part.getOffset().subtract(this.getOffset(state));
    }

    @Override
    public Vec3i getOffset(BlockState state) {
        return state.getValue(this.getPart()).getOffset();
    }

    @Override
    public boolean isMainPart(BlockState state) {
        return this.getOffset(state).equals(this.getMainPartOffset());
    }

    @Override
    public BlockPos getMainPartPos(BlockPos pos, BlockState state) {
        return pos.subtract(this.getOffset(state)).offset(this.getMainPartOffset());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!hasEnoughSpace(context.getClickedPos(), context.getLevel())) return null;
        return this.getPlacementState(context);
    }

    @Nullable
    public BlockState getPlacementState(BlockPlaceContext context) {
        return super.getStateForPlacement(context);
    }
}

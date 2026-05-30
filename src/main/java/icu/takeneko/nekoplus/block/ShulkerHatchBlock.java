package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jspecify.annotations.Nullable;

public class ShulkerHatchBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<ShulkerHatchBlock> CODEC = simpleCodec(ShulkerHatchBlock::new);

    public ShulkerHatchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<ShulkerHatchBlock> codec() {
        return CODEC;
    }

//    @Override
//    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
//        return canSurvive(level, pos, state.getValue(FACING));
//    }
//
//    public static boolean canSurvive(LevelReader level, BlockPos pos, Direction facing) {
//        BlockPos relativePos = pos.relative(facing.getOpposite());
//        BlockState relativeState = level.getBlockState(relativePos);
//        return relativeState.isFaceSturdy(level, relativePos, facing);
//    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var state = this.defaultBlockState();
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var directions = context.getNearestLookingDirections();

        for (var direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                state = state.setValue(FACING, direction);
                if (state.canSurvive(level, pos)) {
                    return state;
                }
            }
        }

        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}

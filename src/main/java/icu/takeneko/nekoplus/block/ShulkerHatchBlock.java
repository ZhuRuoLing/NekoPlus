package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import dev.anvilcraft.lib.v2.util.ShapeUtil;
import icu.takeneko.nekoplus.all.NPTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class ShulkerHatchBlock extends Block {
    public static final VoxelShape SHAPE_N = box(
        1, 0, 0,
        15, 16, 2
    );

    public static final VoxelShape[] SHAPES = new VoxelShape[6];

    static {
        for (Direction value : Direction.values()) {
            if (value == Direction.NORTH) {
                SHAPES[value.ordinal()] = SHAPE_N;
                continue;
            }
            if (value.getAxis() == Direction.Axis.Z) {
                SHAPES[value.ordinal()] = ShapeUtil.rotate(Direction.Axis.Y, value.getOpposite().toYRot(), SHAPE_N);
                continue;
            }
            SHAPES[value.ordinal()] = ShapeUtil.rotate(Direction.Axis.Y, value.toYRot(), SHAPE_N);
        }
    }

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final MapCodec<ShulkerHatchBlock> CODEC = simpleCodec(ShulkerHatchBlock::new);

    public ShulkerHatchBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<ShulkerHatchBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(FACING).ordinal()];
    }

    @Override
    protected void neighborChanged(
        BlockState state,
        Level level,
        BlockPos pos,
        Block block,
        @Nullable Orientation orientation,
        boolean movedByPiston
    ) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING))).is(NPTags.Blocks.NESTED_SHULKER_BLOCK);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        if (!facing.getAxis().isHorizontal()) return null;
        if (level.getBlockState(pos.relative(facing.getOpposite())).is(NPTags.Blocks.NESTED_SHULKER_BLOCK)) {
            return defaultBlockState().setValue(FACING, facing.getOpposite());
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}

package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import dev.anvilcraft.lib.v2.util.ShapeUtil;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.all.NPTags;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.block.tile.ShulkerHatchBlockEntity;
import icu.takeneko.nekoplus.foundation.block.NPTranslucentEntityBlock;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class ShulkerHatchBlock extends NPTranslucentEntityBlock {
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
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof ShulkerHatchBlockEntity entity) {
            entity.eject(player);
        }
        super.attack(state, level, pos, player);
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult hitResult
    ) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof ShulkerHatchBlockEntity entity) {
            entity.insert(player);
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPES[state.getValue(FACING).ordinal()];
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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerHatchBlockEntity(NPBlockEntities.SHULKER_HATCH.get(), pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState blockState,
        BlockEntityType<T> type
    ) {
        if (level instanceof ServerLevel) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<ShulkerHatchBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}

package icu.takeneko.highenergyanvilology.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.HEHatchBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.HEHatchTypes;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.HatchType;
import icu.takeneko.highenergyanvilology.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class HEHatchBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private final HatchType<?> type;
    private final boolean isInput;

    public HEHatchBlock(Properties properties, HatchType<?> type, boolean isInput) {
        super(properties);
        this.type = type;
        this.isInput = isInput;
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(i -> new HEHatchBlock(properties, HEHatchTypes.ITEM, false));
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof HEHatchBlockEntity<?> hatchBlockEntity) {
            hatchBlockEntity.onRemoved();
        }
        level.invalidateCapabilities(pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<AnvilonEmitterBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HEHatchBlockEntity<>(type.getHostType(isInput), pos, state, type, isInput);
    }
}

package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.block.tile.NPHatchBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.NPHatchTypes;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchType;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class NPHatchBlock extends BaseEntityBlock implements NPUIBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private final HatchType<?> type;
    private final boolean isInput;

    public NPHatchBlock(Properties properties, HatchType<?> type, boolean isInput) {
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
        return simpleCodec(i -> new NPHatchBlock(properties, NPHatchTypes.ITEM, false));
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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NPHatchBlockEntity<>(type.getHostType(isInput), pos, state, type, isInput);
    }
}

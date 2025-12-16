package icu.takeneko.highenergyanvilology.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.block.entity.StellarEngineBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.HETranslucentEntityBlock;
import icu.takeneko.highenergyanvilology.foundation.block.entity.SpecialRendererBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class StellarEngineBlock extends HETranslucentEntityBlock implements SpecialRendererBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public StellarEngineBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(StellarEngineBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        return state.setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StellarEngineBlockEntity(HEBlockEntities.STELLAR_ENGINE.get(), pos, state);
    }
}

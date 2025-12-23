package icu.takeneko.highenergyanvilology.block;

import com.lowdragmc.lowdraglib.gui.factory.BlockEntityUIFactory;
import com.mojang.serialization.MapCodec;
import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.HETranslucentEntityBlock;
import icu.takeneko.highenergyanvilology.foundation.block.entity.SpecialRendererBlock;
import icu.takeneko.highenergyanvilology.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class AnvilonEmitterBlock extends HETranslucentEntityBlock implements SimpleWaterloggedBlock, SpecialRendererBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape SHAPE = Shapes.or(
        Shapes.join(
            Shapes.block(),
            Shapes.or(
                Block.box(0, 1, 1, 16, 15, 15),
                Block.box(1, 0, 1, 15, 16, 15),
                Block.box(1, 1, 0, 15, 15, 16)
            ),
            BooleanOp.NOT_SAME
        ),
        Block.box(5, 4, 5, 11, 12, 11)
    );

    public AnvilonEmitterBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<AnvilonEmitterBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level instanceof ServerLevel) {
            if (level.getBlockEntity(pos) instanceof AnvilonEmitterBlockEntity be) {
                Containers.dropContents(level, pos, be.getItemHandler().getStacks());
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel) {
            if (level.getBlockEntity(pos) instanceof AnvilonEmitterBlockEntity be) {
                BlockEntityUIFactory.INSTANCE.openUI(be, (ServerPlayer) player);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    protected FluidState getFluidState(BlockState p_313789_) {
        return p_313789_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(true) : super.getFluidState(p_313789_);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(AnvilonEmitterBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AnvilonEmitterBlockEntity(HEBlockEntities.ANVILION_EMITTER.get(), blockPos, blockState);
    }
}

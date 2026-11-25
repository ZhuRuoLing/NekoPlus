package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.AnimatedItemDisplayTableBlockEntity;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AnimatedItemDisplayTableBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(2, 0, 2, 14, 2, 14),
        Block.box(3, 2, 3, 13, 3, 13),
        Block.box(5, 3, 5, 11, 11, 11),
        Block.box(1, 11, 1, 15, 14, 15)
    );

    public AnimatedItemDisplayTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(AnimatedItemDisplayTableBlock::new);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() && type == NPBlockEntities.ANIMATED_ITEM_DISPLAY_TABLE.get()) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<AnimatedItemDisplayTableBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        updateRedstoneSignal(level, pos);
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
        updateRedstoneSignal(level, pos);
    }

    private void updateRedstoneSignal(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AnimatedItemDisplayTableBlockEntity blockEntity) {
            blockEntity.setRedstoneSignal(level.getBestNeighborSignal(pos));
        }
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack stack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hitResult
    ) {
        if (stack.isEmpty()) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (!level.isClientSide()
            && level.getBlockEntity(pos) instanceof AnimatedItemDisplayTableBlockEntity blockEntity
            && blockEntity.placeItem(stack)
            && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty()) return InteractionResult.PASS;
        if (level.getBlockEntity(pos) instanceof AnimatedItemDisplayTableBlockEntity blockEntity && blockEntity.hasDisplayItem()) {
            if (!level.isClientSide()) {
                ItemStack stack = blockEntity.takeItem();
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AnimatedItemDisplayTableBlockEntity(NPBlockEntities.ANIMATED_ITEM_DISPLAY_TABLE.get(), pos, state);
    }
}

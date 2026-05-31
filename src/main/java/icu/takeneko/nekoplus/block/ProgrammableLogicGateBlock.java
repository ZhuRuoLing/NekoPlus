package icu.takeneko.nekoplus.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.util.Util;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.HorizontalFacingStrategy;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.IOrientableBlock;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.IOrientationStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class ProgrammableLogicGateBlock extends BaseEntityBlock implements NPUIBlock, IOrientableBlock {

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 4, 16);
    public static final IOrientationStrategy ORIENTATION_STRATEGY = HorizontalFacingStrategy.INSTANCE;

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty RED_ENABLE = BooleanProperty.create("red_enable");
    public static final BooleanProperty GREEN_ENABLE = BooleanProperty.create("green_enable");
    public static final BooleanProperty BLUE_ENABLE = BooleanProperty.create("blue_enable");
    public static final BooleanProperty WHITE_ENABLE = BooleanProperty.create("white_enable");

    public static final BooleanProperty[] PROPERTIES_ENABLE = {
        ProgrammableLogicGateBlock.WHITE_ENABLE,
        ProgrammableLogicGateBlock.BLUE_ENABLE,
        ProgrammableLogicGateBlock.GREEN_ENABLE,
        ProgrammableLogicGateBlock.RED_ENABLE,
    };

    public ProgrammableLogicGateBlock(Properties properties) {
        super(properties);
        registerDefaultState(
            getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(RED_ENABLE, false)
                .setValue(GREEN_ENABLE, false)
                .setValue(BLUE_ENABLE, false)
                .setValue(WHITE_ENABLE, false)
        );
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, RED_ENABLE, GREEN_ENABLE, BLUE_ENABLE, WHITE_ENABLE);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(ModItems.DISK.asItem())) {
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }
            if (level.getBlockEntity(pos) instanceof ProgrammableLogicGateBlockEntity blockEntity) {
                return blockEntity.useDisk(level, player, hand, player.getItemInHand(hand), hitResult);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel) {
            if (level.getBlockEntity(pos) instanceof ProgrammableLogicGateBlockEntity be) {
                BlockUIMenuType.openUI((ServerPlayer) player, pos);
                return Util.sidedSuccess(level);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void neighborChanged(
        BlockState state,
        Level level,
        BlockPos pos,
        Block block,
        @org.jspecify.annotations.Nullable Orientation orientation,
        boolean movedByPiston
    ) {
        if (level instanceof ServerLevel) {
            level.scheduleTick(pos, state.getBlock(), 1);
        }
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (level.getBlockEntity(pos) instanceof ProgrammableLogicGateBlockEntity be) {
            return be.getSignal(direction.getOpposite());
        }
        return 0;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if (level.getBlockEntity(pos) instanceof ProgrammableLogicGateBlockEntity be) {
            be.updatePins();
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ProgrammableLogicGateBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ProgrammableLogicGateBlockEntity(NPBlockEntities.PROGRAMMABLE_LOGIC_GATE.get(), blockPos, blockState);
    }

    @Override
    public IOrientationStrategy getOrientationStrategy() {
        return ORIENTATION_STRATEGY;
    }
}

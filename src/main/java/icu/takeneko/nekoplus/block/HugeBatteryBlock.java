package icu.takeneko.nekoplus.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import dev.anvilcraft.lib.v2.util.ShapeUtil;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.block.state.Cube3x3PartHalf;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.HugeBatteryBlockEntity;
import icu.takeneko.nekoplus.foundation.block.NPSimpleMultiPartBlock;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class HugeBatteryBlock extends NPSimpleMultiPartBlock<Cube3x3PartHalf> implements EntityBlock, NPUIBlock {
    public static final EnumProperty<Cube3x3PartHalf> PART = EnumProperty.create("part", Cube3x3PartHalf.class);
    public static final BooleanProperty OVERLOAD = IPowerComponent.OVERLOAD;
    public static final BooleanProperty DISCHARGING = BatteryBlock.DISCHARGING;


    private static final VoxelShape SHAPE_MID_N = Block.box(0, 0, 9, 16, 16, 16);
    private static final VoxelShape SHAPE_MID_W = ShapeUtil.rotate(Direction.Axis.Y, 90, SHAPE_MID_N);
    private static final VoxelShape SHAPE_MID_S = ShapeUtil.rotate(Direction.Axis.Y, 180, SHAPE_MID_N);
    private static final VoxelShape SHAPE_MID_E = ShapeUtil.rotate(Direction.Axis.Y, 270, SHAPE_MID_N);
    private static final VoxelShape SHAPE_MID_NW = Block.box(9, 0, 9, 16, 16, 16);
    private static final VoxelShape SHAPE_MID_SW = ShapeUtil.rotate(Direction.Axis.Y, 90, SHAPE_MID_NW);
    private static final VoxelShape SHAPE_MID_SE = ShapeUtil.rotate(Direction.Axis.Y, 180, SHAPE_MID_NW);
    private static final VoxelShape SHAPE_MID_NE = ShapeUtil.rotate(Direction.Axis.Y, 270, SHAPE_MID_NW);

    private static final VoxelShape SHAPE_BTM_BASE = Block.box(0, 0, 0, 16, 12, 16);
    private static final VoxelShape SHAPE_BTM_N = Shapes.or(SHAPE_BTM_BASE, SHAPE_MID_N);
    private static final VoxelShape SHAPE_BTM_W = ShapeUtil.rotate(Direction.Axis.Y, 90, SHAPE_BTM_N);
    private static final VoxelShape SHAPE_BTM_S = ShapeUtil.rotate(Direction.Axis.Y, 180, SHAPE_BTM_N);
    private static final VoxelShape SHAPE_BTM_E = ShapeUtil.rotate(Direction.Axis.Y, 270, SHAPE_BTM_N);
    private static final VoxelShape SHAPE_BTM_NW = Shapes.or(SHAPE_BTM_BASE, SHAPE_MID_NW);
    private static final VoxelShape SHAPE_BTM_SW = ShapeUtil.rotate(Direction.Axis.Y, 90, SHAPE_BTM_NW);
    private static final VoxelShape SHAPE_BTM_SE = ShapeUtil.rotate(Direction.Axis.Y, 180, SHAPE_BTM_NW);
    private static final VoxelShape SHAPE_BTM_NE = ShapeUtil.rotate(Direction.Axis.Y, 270, SHAPE_BTM_NW);

    private static final VoxelShape SHAPE_TOP_BASE = Block.box(0, 4, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_TOP_N = Shapes.or(SHAPE_TOP_BASE, SHAPE_MID_N);
    private static final VoxelShape SHAPE_TOP_W = ShapeUtil.rotate(Direction.Axis.Y, 90, SHAPE_TOP_N);
    private static final VoxelShape SHAPE_TOP_S = ShapeUtil.rotate(Direction.Axis.Y, 180, SHAPE_TOP_N);
    private static final VoxelShape SHAPE_TOP_E = ShapeUtil.rotate(Direction.Axis.Y, 270, SHAPE_TOP_N);
    private static final VoxelShape SHAPE_TOP_NW = Shapes.or(SHAPE_TOP_BASE, SHAPE_MID_NW);
    private static final VoxelShape SHAPE_TOP_SW = ShapeUtil.rotate(Direction.Axis.Y, 90, SHAPE_TOP_NW);
    private static final VoxelShape SHAPE_TOP_SE = ShapeUtil.rotate(Direction.Axis.Y, 180, SHAPE_TOP_NW);
    private static final VoxelShape SHAPE_TOP_NE = ShapeUtil.rotate(Direction.Axis.Y, 270, SHAPE_TOP_NW);


    public HugeBatteryBlock(Properties properties) {
        super(properties);
        registerDefaultState(
            getStateDefinition()
                .any()
                .setValue(PART, Cube3x3PartHalf.BOTTOM_CENTER)
                .setValue(OVERLOAD, true)
                .setValue(DISCHARGING, false)
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(PART)) {
            case BOTTOM_W -> SHAPE_BTM_W;
            case BOTTOM_E -> SHAPE_BTM_E;
            case BOTTOM_N -> SHAPE_BTM_N;
            case BOTTOM_S -> SHAPE_BTM_S;
            case BOTTOM_WN -> SHAPE_BTM_NW;
            case BOTTOM_WS -> SHAPE_BTM_SW;
            case BOTTOM_EN -> SHAPE_BTM_NE;
            case BOTTOM_ES -> SHAPE_BTM_SE;
            case MID_W -> SHAPE_MID_W;
            case MID_E -> SHAPE_MID_E;
            case MID_N -> SHAPE_MID_N;
            case MID_S -> SHAPE_MID_S;
            case MID_WN -> SHAPE_MID_NW;
            case MID_WS -> SHAPE_MID_SW;
            case MID_EN -> SHAPE_MID_NE;
            case MID_ES -> SHAPE_MID_SE;
            case TOP_W -> SHAPE_TOP_W;
            case TOP_E -> SHAPE_TOP_E;
            case TOP_N -> SHAPE_TOP_N;
            case TOP_S -> SHAPE_TOP_S;
            case TOP_WN -> SHAPE_TOP_NW;
            case TOP_WS -> SHAPE_TOP_SW;
            case TOP_EN -> SHAPE_TOP_NE;
            case TOP_ES -> SHAPE_TOP_SE;
            default -> Shapes.block();
        };
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.empty();
    }

    @Override
    public Property<Cube3x3PartHalf> getPart() {
        return PART;
    }

    @Override
    public Cube3x3PartHalf[] getParts() {
        return Cube3x3PartHalf.values();
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState blockState,
        BlockEntityType<T> type
    ) {
        if (blockState.getValue(PART) == Cube3x3PartHalf.MID_CENTER) {
            if (level instanceof ServerLevel) {
                return (BlockEntityTicker<T>) BlockEntityUtil.<HugeBatteryBlockEntity>createTicker();
            }
        }
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult hitResult
    ) {
        if (level instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
            BlockUIMenuType.openUI(serverPlayer, getMainPartPos(pos, state));
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public Vec3i getMainPartOffset() {
        return new Vec3i(0, 1, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART, OVERLOAD, DISCHARGING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        if (blockState.getValue(PART) == Cube3x3PartHalf.MID_CENTER) {
            return new HugeBatteryBlockEntity(NPBlockEntities.HUGE_BATTERY.get(), worldPosition, blockState);
        }
        return null;
    }

    @Override
    protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }
}

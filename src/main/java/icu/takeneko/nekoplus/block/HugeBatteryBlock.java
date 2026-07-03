package icu.takeneko.nekoplus.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.block.state.Cube3x3PartHalf;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.HugeBatteryBlockEntity;
import icu.takeneko.nekoplus.foundation.block.NPSimpleMultiPartBlock;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class HugeBatteryBlock extends NPSimpleMultiPartBlock<Cube3x3PartHalf> implements EntityBlock, NPUIBlock {
    public static final EnumProperty<Cube3x3PartHalf> PART = EnumProperty.create("part", Cube3x3PartHalf.class);
    public static final BooleanProperty OVERLOAD = IPowerComponent.OVERLOAD;
    public static final BooleanProperty DISCHARGING = BatteryBlock.DISCHARGING;

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

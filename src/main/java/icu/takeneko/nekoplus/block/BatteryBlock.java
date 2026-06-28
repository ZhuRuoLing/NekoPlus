package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.BatteryBlockEntity;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.Nullable;

public class BatteryBlock extends BaseEntityBlock {
    public static final BooleanProperty OVERLOAD = IPowerComponent.OVERLOAD;
    public static final BooleanProperty DISCHARGING = BooleanProperty.create("discharging");

    public BatteryBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
            .setValue(OVERLOAD, true)
            .setValue(DISCHARGING, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BatteryBlock::new);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OVERLOAD, DISCHARGING);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState blockState,
        BlockEntityType<T> type
    ) {
        if (level instanceof ServerLevel) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<BatteryBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BatteryBlockEntity(NPBlockEntities.BATTERY.get(), pos, state);
    }
}

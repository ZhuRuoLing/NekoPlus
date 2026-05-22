package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.IOrientationStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class OverclockIndicatorBlock extends BaseEntityBlock {
    public static final IntegerProperty OVERCLOCK_LEVEL = IntegerProperty.create("overclock_level", 0, 4);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final IntegerProperty SPIN = IOrientationStrategy.SPIN;

    public OverclockIndicatorBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition()
            .any()
            .setValue(OVERCLOCK_LEVEL, 0)
            .setValue(FACING, Direction.NORTH)
            .setValue(SPIN, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OVERCLOCK_LEVEL, FACING, SPIN);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(OverclockIndicatorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
}

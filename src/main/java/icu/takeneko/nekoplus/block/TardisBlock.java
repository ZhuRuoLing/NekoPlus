package icu.takeneko.nekoplus.block;

import dev.dubhe.anvilcraft.util.Util;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.all.NPBlockStateProperties;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;
import icu.takeneko.nekoplus.block.property.Part3;
import icu.takeneko.nekoplus.foundation.block.NPSimpleMultiPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class TardisBlock extends NPSimpleMultiPartBlock<Part3> implements EntityBlock {
    public static final EnumProperty<Part3> PART = NPBlockStateProperties.PART_3;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public TardisBlock(Properties properties) {
        super(properties);
        registerDefaultState(
            getStateDefinition()
                .any()
                .setValue(PART, Part3.BOTTOM)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public Property<Part3> getPart() {
        return PART;
    }

    @Override
    public Part3[] getParts() {
        return Part3.values();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART, FACING);
    }

    @Override
    public boolean hasEnoughSpace(BlockPos pos, LevelReader level) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (!level.getBlockState(pos.offset(dx, 0, dz)).canBeReplaced()) {
                    return false;
                }
            }
        }
        return pos.getY() < level.getHeight() - 2
            && level.getBlockState(pos.above()).canBeReplaced()
            && level.getBlockState(pos.above(2)).canBeReplaced();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!hasEnoughSpace(context.getClickedPos(), context.getLevel())) return null;
        return super.getPlacementState(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockPos upper = pos.above();
        level.setBlock(upper, state.setValue(PART, Part3.MIDDLE), 3);
        upper = upper.above();
        level.setBlock(upper, state.setValue(PART, Part3.TOP), 3);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == Part3.BOTTOM ? new TardisBlockEntity(NPBlockEntities.TARDIS.get(), pos, state) : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel) {
            Part3 part = state.getValue(PART);
            pos = switch (part) {
                case TOP -> pos.below(2);
                case MIDDLE -> pos.below(1);
                case BOTTOM -> pos;
            };
            if (level.getBlockEntity(pos) instanceof TardisBlockEntity tardis) {
                tardis.onClick();
                return Util.sidedSuccess(level);
            }
        }

        return InteractionResult.SUCCESS;
    }
}

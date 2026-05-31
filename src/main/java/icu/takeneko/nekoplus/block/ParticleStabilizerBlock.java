package icu.takeneko.nekoplus.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.util.Util;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class ParticleStabilizerBlock extends BaseEntityBlock implements NPUIBlock {
    public static final BooleanProperty COOLING = BooleanProperty.create("cooling");
    public static final BooleanProperty OVERLOAD = BooleanProperty.create("overload");

    public ParticleStabilizerBlock(Properties properties) {
        super(properties);
        registerDefaultState(
            getStateDefinition().any()
                .setValue(COOLING, false)
                .setValue(OVERLOAD, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Block.simpleCodec(ParticleStabilizerBlock::new);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COOLING, OVERLOAD);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof ParticleStabilizerBlockEntity blockEntity) {
            if (blockEntity.isOverload()) return;
            if (blockEntity.getState() != ParticleStabilizerBlockEntity.State.COOLING) return;
            RandomSource randomsource = level.getRandom();
            for (int i = 0; i < 2; i++) {
                double dx = 0.5 + Mth.randomBetween(randomsource, -0.65f, 0.65f) * (random.nextBoolean() ? 1 : -1);
                double dy = 0.55 + Mth.randomBetween(randomsource, -0.65f, 0.75f) * (random.nextBoolean() ? 1 : -1);
                double dz = 0.5 + Mth.randomBetween(randomsource, -0.65f, 0.65f) * (random.nextBoolean() ? 1 : -1);
                level.addParticle(
                    ParticleTypes.SNOWFLAKE,
                    pos.getX() + dx,
                    pos.getY() + dy,
                    pos.getZ() + dz,
                    Mth.randomBetween(randomsource, -0.75f, 0.75f) * 0.08F,
                    0.05F,
                    Mth.randomBetween(randomsource, -0.75f, 0.75f) * 0.08F
                );
            }
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<ParticleStabilizerBlockEntity>createTicker();
        }
        return null;
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
        if (stack.is(NPItems.CHARGED_LEVITATION_POWDER)) {
            if (level.isClientSide()) {
                level.playSound(player, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS);
                return Util.sidedSuccess(level);
            }
            if (!(level.getBlockEntity(pos) instanceof ParticleStabilizerBlockEntity blockEntity)) return InteractionResult.FAIL;
            blockEntity.toggleOverclock();
            return InteractionResult.CONSUME;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel) {
            if (level.getBlockEntity(pos) instanceof ParticleStabilizerBlockEntity be) {
                BlockUIMenuType.openUI((ServerPlayer) player, pos);
                return Util.sidedSuccess(level);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (level.getBlockEntity(pos) instanceof ParticleStabilizerBlockEntity blockEntity) {
            return blockEntity.isOverload() ? 0 : switch (blockEntity.getState()) {
                case COOLING -> 8;
                case WORKING -> 15;
            };
        }
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ParticleStabilizerBlockEntity(NPBlockEntities.PARTICLE_STABILIZER.get(), pos, state);
    }
}

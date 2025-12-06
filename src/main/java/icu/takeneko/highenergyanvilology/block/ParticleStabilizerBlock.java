package icu.takeneko.highenergyanvilology.block;

import com.lowdragmc.lowdraglib.gui.factory.BlockEntityUIFactory;
import com.mojang.serialization.MapCodec;
import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.block.entity.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.entity.SpecialRendererBlock;
import icu.takeneko.highenergyanvilology.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ParticleStabilizerBlock extends BaseEntityBlock implements SpecialRendererBlock {
    public ParticleStabilizerBlock(Properties properties) {
        super(properties);
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
            return (BlockEntityTicker<T>) BlockEntityUtil.<AnvilonEmitterBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel) {
            if (level.getBlockEntity(pos) instanceof ParticleStabilizerBlockEntity be) {
                BlockEntityUIFactory.INSTANCE.openUI(be, (ServerPlayer) player);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
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
        return new ParticleStabilizerBlockEntity(HEBlockEntities.PARTICLE_STABILIZER.get(), pos, state);
    }
}

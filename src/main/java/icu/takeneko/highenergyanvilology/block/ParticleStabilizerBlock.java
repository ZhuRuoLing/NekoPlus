package icu.takeneko.highenergyanvilology.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ParticleStabilizerBlockEntity(HEBlockEntities.PARTICLE_STABILIZER.get(), pos, state);
    }
}

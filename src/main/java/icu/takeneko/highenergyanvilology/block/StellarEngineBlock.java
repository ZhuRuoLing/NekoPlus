package icu.takeneko.highenergyanvilology.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.block.entity.StellarEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StellarEngineBlock extends BaseEntityBlock {
    public StellarEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(StellarEngineBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StellarEngineBlockEntity(HEBlockEntities.STELLAR_ENGINE.get(), pos, state);
    }
}

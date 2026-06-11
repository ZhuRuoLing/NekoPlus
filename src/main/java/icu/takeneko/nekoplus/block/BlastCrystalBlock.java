package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.BlastCrystalBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BlastCrystalBlock extends BaseEntityBlock {
    @Getter
    private final CrackStage stage;

    public BlastCrystalBlock(Properties properties) {
        this(properties, CrackStage.NORMAL);
    }

    public BlastCrystalBlock(Properties properties, CrackStage stage) {
        super(properties);
        this.stage = stage;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BlastCrystalBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlastCrystalBlockEntity(NPBlockEntities.BLAST_CRYSTAL.get(), blockPos, blockState);
    }

    public enum CrackStage {
        NORMAL, DAMAGED, CRACKED
    }
}

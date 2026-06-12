package icu.takeneko.nekoplus.block;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.BlastCrystalBlockEntity;
import icu.takeneko.nekoplus.util.BlockEntityUtil;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;

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

    @Override
    protected void onExplosionHit(
        BlockState state,
        ServerLevel level,
        BlockPos pos,
        Explosion explosion,
        BiConsumer<ItemStack, BlockPos> onHit
    ) {
        if (level.getBlockEntity(pos) instanceof BlastCrystalBlockEntity blockEntity) {
            blockEntity.handleExplosionHit(level, pos, explosion);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState blockState,
        BlockEntityType<T> type
    ) {
        if (level instanceof ServerLevel) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<BlastCrystalBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    public void onBlockExploded(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);
    }

    public enum CrackStage {
        NORMAL, DAMAGED, CRACKED;

        public BlockState nextState() {
            return switch (this) {
                case NORMAL -> NPBlocks.CRACKED_BLAST_CRYSTAL.getDefaultState();
                case DAMAGED -> NPBlocks.DAMAGED_BLAST_CRYSTAL.getDefaultState();
                case CRACKED -> Blocks.AIR.defaultBlockState();
            };
        }
    }
}

package icu.takeneko.nekoplus.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.TestBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class TestBlock extends BaseEntityBlock implements NPUIBlock {
    public static final MapCodec<TestBlock> CODEC = simpleCodec(TestBlock::new);

    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TestBlockEntity(NPBlockEntities.TEST.get(), pos, state);
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
            BlockUIMenuType.openUI(serverPlayer, pos);
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.SUCCESS;
    }
}

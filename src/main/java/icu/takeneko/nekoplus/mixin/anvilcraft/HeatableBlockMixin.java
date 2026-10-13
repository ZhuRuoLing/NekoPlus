package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.heatable.HeatableBlock;
import icu.takeneko.nekoplus.internal.HeatableBlockEntityInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeatableBlock.class)
public class HeatableBlockMixin {

    @Inject(method = "neighborChanged", at = @At("HEAD"))
    private void updateInsulation(
        BlockState state,
        Level level,
        BlockPos pos,
        Block block,
        @Nullable Orientation orientation,
        boolean movedByPiston,
        CallbackInfo ci
    ) {
        if (level.isClientSide()) return;
        if (level.getBlockEntity(pos) instanceof HeatableBlockEntityInternals.Extension extension) {
            extension.updateInsulation(level, pos);
        }
    }
}

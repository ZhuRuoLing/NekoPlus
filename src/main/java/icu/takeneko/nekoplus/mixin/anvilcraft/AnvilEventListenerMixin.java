package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.event.anvil.AnvilEventListener;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(AnvilEventListener.class)
public class AnvilEventListenerMixin {
    @WrapOperation(
        method = "brokeBlock",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/util/BreakBlockUtil;drop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Ljava/util/List;")
    )
    private static List<ItemStack> dropSlicedSilicon(
        ServerLevel level,
        BlockPos pos,
        Operation<List<ItemStack>> original
    )   {
        if (level.getBlockState(pos).is(NPBlocks.SILICON_BLOCK)) {
            return List.of(NPItems.SILICON_WAFER.asStack(4));
        }
        return original.call(level, pos);
    }
}

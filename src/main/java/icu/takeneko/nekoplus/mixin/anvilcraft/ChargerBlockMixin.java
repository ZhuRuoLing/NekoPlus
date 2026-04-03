package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.ChargerBlock;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.internal.ChargerBlockEntityInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChargerBlock.class)
public class ChargerBlockMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    void activateOverclock(
        ItemStack stack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit,
        CallbackInfoReturnable<ItemInteractionResult> cir
    ) {
        if (stack.is(NPItems.CHARGED_LEVITATION_POWDER)) {
            if (level.isClientSide) {
                level.playSound(player, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS);
                cir.setReturnValue(ItemInteractionResult.sidedSuccess(true));
                return;
            }
            if (!(level.getBlockEntity(pos) instanceof ChargerBlockEntityInternals.Extension extension)) return;
            extension.toggleOverclock();
            cir.setReturnValue(ItemInteractionResult.CONSUME);
            cir.cancel();
        }
    }
}

package icu.takeneko.nekoplus.mixin.anvilcraft;


import dev.dubhe.anvilcraft.block.power.consumer.SmartBlockPlacerBlock;
import dev.dubhe.anvilcraft.util.Util;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.internal.SmartBlockPlacerBlockEntityInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmartBlockPlacerBlock.class)
public class SmartBlockPlacerBlockMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    void activateOverclock(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hitResult,
        CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (player.getItemInHand(hand).is(NPItems.CHARGED_LEVITATION_POWDER)) {
            if (level.isClientSide()) {
                level.playSound(player, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS);
                cir.setReturnValue(Util.sidedSuccess(level));
                return;
            }
            if (!(level.getBlockEntity(pos) instanceof SmartBlockPlacerBlockEntityInternals.Extension extension)) return;
            extension.toggleOverclock();
            cir.setReturnValue(InteractionResult.CONSUME);
            cir.cancel();
        }
    }
}

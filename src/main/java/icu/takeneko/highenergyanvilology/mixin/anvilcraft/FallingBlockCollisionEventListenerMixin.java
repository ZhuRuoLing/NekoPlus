package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.event.FallingBlockCollisionEventListener;
import icu.takeneko.highenergyanvilology.all.HEEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockCollisionEventListener.class)
public class FallingBlockCollisionEventListenerMixin {
    @Inject(
        method = "anvilCollisionCraft",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void inj(AnvilEvent.CollisionBlock event, CallbackInfo ci) {
        //AnvilEvent.CollisionBlock is not cancellable so a mixin were used to achieve this
        if (HEEvents.on(event)) {
            ci.cancel();
        }
    }
}

package icu.takeneko.nekoplus.mixin.eyelib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.tt432.eyelib.client.render.controller.RenderControllerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderControllerEntry.class)
public class RenderControllerEntryMixin {
    @WrapOperation(
        method = "lambda$new$8",
        at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z")
    )
    private static boolean handleEvent(String instance, Object o, Operation<Boolean> original){
        return instance.equals("RenderControllerManager");
    }
}

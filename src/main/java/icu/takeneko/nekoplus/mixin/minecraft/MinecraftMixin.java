package icu.takeneko.nekoplus.mixin.minecraft;

import icu.takeneko.nekoplus.util.NPUIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    void clientRenderThreadSetup(GameConfig gameConfig, CallbackInfo ci) {
        NPUIUtils.clientSetup();
    }
}

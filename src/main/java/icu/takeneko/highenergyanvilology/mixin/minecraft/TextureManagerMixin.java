package icu.takeneko.highenergyanvilology.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

    @WrapOperation(
        method = "loadTexture",
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V")
    )
    void doNotPrintLogBecauseThisIsUgly(Logger instance, String s, Object o1, Object o2, Operation<Void> original) {
        ResourceLocation path = (ResourceLocation) o1;
        if ("complex".equals(path.getNamespace())) return;
        if (HEAnvilology.MODID.equals(path.getNamespace())) return;
        if (path.getPath().contains(HEAnvilology.MODID)) return;
        original.call(instance, s, o1, o2);
    }
}

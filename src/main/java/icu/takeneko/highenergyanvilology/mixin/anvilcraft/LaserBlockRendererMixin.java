package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.client.renderer.RenderState;
import dev.dubhe.anvilcraft.client.renderer.blockentity.LaserBlockRenderer;
import icu.takeneko.highenergyanvilology.client.renderer.laser.HELaserCompiler;
import icu.takeneko.highenergyanvilology.client.renderer.laser.HELaserState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LaserBlockRenderer.class)
public class LaserBlockRendererMixin {

    @Inject(
        method = "render(Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At("HEAD"),
        cancellable = true
    )
    void gentlyOverwriteRender(
        BaseLaserBlockEntity baseLaserBlockEntity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay,
        CallbackInfo ci
    ) {
        if (RenderState.isEnhancedRenderingAvailable()) return;
        poseStack.pushPose();
        HELaserState laserState = HELaserState.create(baseLaserBlockEntity, poseStack);
        if (laserState != null) {
            HELaserCompiler.compile(
                laserState,
                buffer::getBuffer
            );
        }
        if (buffer instanceof MultiBufferSource.BufferSource bs) {
            bs.endBatch(RenderType.translucent());
        }
        poseStack.popPose();
        ci.cancel();
    }
}

package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.client.renderer.RenderState;
import dev.dubhe.anvilcraft.client.renderer.blockentity.LaserBlockRenderer;
import icu.takeneko.nekoplus.client.renderer.laser.NPLaserCompiler;
import icu.takeneko.nekoplus.client.renderer.laser.NPLaserState;
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
        NPLaserState laserState = NPLaserState.create(baseLaserBlockEntity, poseStack);
        if (laserState != null) {
            NPLaserCompiler.compile(
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

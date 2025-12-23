package icu.takeneko.highenergyanvilology.client.renderer.tesr;

import com.mojang.blaze3d.vertex.PoseStack;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AnvilonEmitterBlockEntityRenderer extends EyelibBlockEntityRenderer<AnvilonEmitterBlockEntity> {

    public AnvilonEmitterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(AnvilonEmitterBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }
}

package icu.takeneko.highenergyanvilology.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.block.entity.StellarEngineBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StellarEngineBlockEntityRenderer extends EyelibBlockEntityRenderer<StellarEngineBlockEntity> {

    public StellarEngineBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(StellarEngineBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(StellarEngineBlockEntity blockEntity) {
        AABB aabb = super.getRenderBoundingBox(blockEntity).inflate(3.5, 0, 3.5);
        return aabb.setMaxY(aabb.maxY + 8);
    }
}

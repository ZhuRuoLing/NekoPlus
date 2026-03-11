package icu.takeneko.nekoplus.client.renderer.tesr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import icu.takeneko.nekoplus.block.TardisBlock;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public class TardisBlockEntityRenderer extends EyelibBlockEntityRenderer<TardisBlockEntity> {
    public TardisBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TardisBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        Direction value = blockEntity.getBlockState().getValue(TardisBlock.FACING);
        poseStack.mulPose(
            Axis.YP.rotationDegrees(
                switch (value) {
                    case NORTH -> 180;
                    case WEST -> 90;
                    case EAST -> 270;
                    default -> 0;
                }
            )
        );
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(TardisBlockEntity blockEntity) {
        AABB aabb = super.getRenderBoundingBox(blockEntity).inflate(1.5, 0, 1.5);
        return aabb.setMaxY(aabb.maxY + 8);
    }
}

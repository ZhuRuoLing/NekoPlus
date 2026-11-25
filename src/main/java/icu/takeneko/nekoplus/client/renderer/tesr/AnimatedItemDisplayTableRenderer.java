package icu.takeneko.nekoplus.client.renderer.tesr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import icu.takeneko.nekoplus.block.tile.AnimatedItemDisplayTableBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class AnimatedItemDisplayTableRenderer implements BlockEntityRenderer<AnimatedItemDisplayTableBlockEntity, AnimatedItemDisplayTableRenderer.RenderState> {
    private final ItemModelResolver itemModelResolver;

    public AnimatedItemDisplayTableRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
        AnimatedItemDisplayTableBlockEntity blockEntity,
        RenderState state,
        float partialTicks,
        Vec3 cameraPosition,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        var level = blockEntity.getLevel();
        state.rotation = Mth.lerp(partialTicks, blockEntity.getPreviousRotation(), blockEntity.getRotation());
        state.scale = Mth.lerp(partialTicks, blockEntity.getPreviousScale(), blockEntity.getScale());
        state.stretch = Mth.lerp(partialTicks, blockEntity.getPreviousStretch(), blockEntity.getStretch());
        this.itemModelResolver.updateForTopItem(
            state.item,
            blockEntity.getDisplayItem(),
            ItemDisplayContext.GROUND,
            level,
            null,
            42
        );
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.item.isEmpty()) return;
        poseStack.pushPose();
        float verticalScale = state.scale * state.stretch;
        float horizontalScale = state.scale / (float) Math.sqrt(state.stretch);
        double heightOffset = -state.item.getModelBoundingBox().minY * verticalScale;
        poseStack.translate(0.5, (14.0 + 2.0) / 16 + heightOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));
        poseStack.scale(horizontalScale, verticalScale, horizontalScale);
        state.item.submit(poseStack, collector, 15728880, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float rotation;
        public float scale = 1;
        public float stretch = 1;
    }
}

package icu.takeneko.nekoplus.client.renderer.bewlr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPAnvilMaterials;
import icu.takeneko.nekoplus.all.NPDataComponents;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.foundation.material.AnvilonType;
import icu.takeneko.nekoplus.util.ClientTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.List;

/// intentionally long class name xD
public class MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public static final ResourceLocation CONTAINER_MODEL = NekoPlus.location("item/magnetic_confinement_vessel_container");
    public static final ResourceLocation CONTENT_MODEL = NekoPlus.location("item/magnetic_confinement_vessel_content");
    public static final ResourceLocation MAGNETIC_MODEL = NekoPlus.location("item/magnetic_confinement_vessel_mag");

    public static final ModelResourceLocation CONTAINER_MODEL_LOCATION = NekoPlus.modelLocation("item/magnetic_confinement_vessel_container");
    public static final ModelResourceLocation CONTENT_MODEL_LOCATION = NekoPlus.modelLocation("item/magnetic_confinement_vessel_content");
    public static final ModelResourceLocation MAGNETIC_MODEL_LOCATION = NekoPlus.modelLocation("item/magnetic_confinement_vessel_mag");

    public static final float SELF_ROTATION_SPEED = 0.03926990f;

    public MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer(
        BlockEntityRenderDispatcher blockEntityRenderDispatcher,
        EntityModelSet entityModelSet
    ) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @Override
    public void renderByItem(
        ItemStack stack,
        ItemDisplayContext displayContext,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay
    ) {
        if (!stack.is(NPItems.MAGNETIC_CONFINEMENT_VESSEL)) {
            super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        float time = mc.getTimer().getGameTimeDeltaPartialTick(true) + ClientTimer.getClientTime();
        ModelManager modelManager = mc.getModelManager();
        BakedModel containerModel = modelManager.getModel(CONTAINER_MODEL_LOCATION);
        BakedModel contentModel = modelManager.getModel(CONTENT_MODEL_LOCATION);
        BakedModel magnetModel = modelManager.getModel(MAGNETIC_MODEL_LOCATION);
        boolean hasContent = stack.getOrDefault(NPDataComponents.CONTAINED_ANVILON_TYPE, NPAnvilMaterials.EMPTY) != NPAnvilMaterials.EMPTY;
        AnvilonType.Contained type = stack.getOrDefault(NPDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE);
        boolean isEntangled = type == AnvilonType.Contained.ENTANGLED;
        boolean isStable = type == AnvilonType.Contained.STABLE;
        renderModel(
            stack,
            containerModel,
            poseStack,
            buffer,
            packedLight,
            packedOverlay
        );
        if (!hasContent) return;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        if (isEntangled) {
            poseStack.mulPose(rotation(time, 0.75f, false));
        } else {
            poseStack.mulPose(rotation(time, -0.75f, false));
        }
        renderModel(
            stack,
            contentModel,
            poseStack,
            buffer,
            LightTexture.FULL_BRIGHT,
            packedOverlay
        );
        poseStack.popPose();

        if (isStable || isEntangled) return;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        for (int i = 0; i < 4; i++) {
            renderMagnet(
                i,
                stack,
                magnetModel,
                poseStack,
                buffer,
                time,
                packedLight,
                packedOverlay
            );
        }
        poseStack.popPose();
    }

    private void renderMagnet(
        int index,
        ItemStack itemStack,
        BakedModel model,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        float time,
        int packedLight,
        int packedOverlay
    ) {
        float decreasedTime = time * 0.05f;
        int color = (index & 1) == 0 ? 0xFFFF5050 : 0xFF5050FF;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(time * SELF_ROTATION_SPEED * 128 + (index * 90)));
        poseStack.translate(0.5, 0, 0);
        poseStack.translate(
            0,
            Math.sin(decreasedTime * 2 + (index & 1) * Math.PI * 0.5) * 0.25,
            0
        );
        poseStack.mulPose(rotation(time, 2, true));
        renderModel(
            itemStack,
            model,
            poseStack,
            bufferSource,
            packedLight,
            packedOverlay,
            color
        );
        poseStack.popPose();
    }

    private Quaternionf rotation(float time, float rotationRate, boolean positive) {
        if (!positive) {
            time *= -1;
        }
        Quaternionf xRot = Axis.XP.rotation(time * SELF_ROTATION_SPEED * rotationRate);
        Quaternionf yRot = Axis.YP.rotation(time * SELF_ROTATION_SPEED * rotationRate);
        Quaternionf zRot = Axis.ZP.rotation(time * SELF_ROTATION_SPEED * rotationRate);
        return xRot.mul(yRot).mul(zRot);
    }

    private void renderModel(
        ItemStack itemStack,
        BakedModel model,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        int packedOverlay
    ) {
        renderModel(
            itemStack,
            model,
            poseStack,
            bufferSource,
            packedLight,
            packedOverlay,
            -1
        );
    }

    private void renderModel(
        ItemStack itemStack,
        BakedModel model,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        int packedOverlay,
        int tint
    ) {
        for (BakedModel pass : model.getRenderPasses(itemStack, true)) {
            for (RenderType renderType : pass.getRenderTypes(itemStack, true)) {
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, true, itemStack.hasFoil());
                this.renderModelLists(
                    model,
                    itemStack,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    vertexConsumer,
                    tint
                );
            }
        }
    }

    public void renderModelLists(
        BakedModel model,
        ItemStack stack,
        int combinedLight,
        int combinedOverlay,
        PoseStack poseStack,
        VertexConsumer buffer,
        int tint
    ) {
        RandomSource randomsource = RandomSource.create();

        for (Direction direction : Direction.values()) {
            randomsource.setSeed(42L);
            this.renderQuadList(poseStack, buffer, model.getQuads(null, direction, randomsource), stack, combinedLight, combinedOverlay, tint);
        }

        randomsource.setSeed(42L);
        this.renderQuadList(poseStack, buffer, model.getQuads(null, null, randomsource), stack, combinedLight, combinedOverlay, tint);
    }

    public void renderQuadList(
        PoseStack poseStack,
        VertexConsumer buffer,
        List<BakedQuad> quads,
        ItemStack itemStack,
        int combinedLight,
        int combinedOverlay,
        int tint
    ) {
        PoseStack.Pose posestack$pose = poseStack.last();

        for (BakedQuad bakedquad : quads) {
            int color = -1;
            if (!itemStack.isEmpty() && bakedquad.isTinted()) {
                if (tint != -1) {
                    color = tint;
                } else {
                    color = Minecraft.getInstance().getItemColors().getColor(itemStack, bakedquad.getTintIndex());
                }
            }

            float f = (float) FastColor.ARGB32.alpha(color) / 255.0F;
            float f1 = (float) FastColor.ARGB32.red(color) / 255.0F;
            float f2 = (float) FastColor.ARGB32.green(color) / 255.0F;
            float f3 = (float) FastColor.ARGB32.blue(color) / 255.0F;
            buffer.putBulkData(posestack$pose, bakedquad, f1, f2, f3, f, combinedLight, combinedOverlay, true); // Neo: pass readExistingColor=true
        }
    }
}

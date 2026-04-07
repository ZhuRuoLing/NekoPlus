package icu.takeneko.nekoplus.foundation.ui.widgets;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import dev.dubhe.anvilcraft.client.init.ModShaders;
import dev.dubhe.anvilcraft.client.support.RenderSupport;
import dev.dubhe.anvilcraft.util.VertexConsumerWithPose;
import icu.takeneko.nekoplus.util.ClientSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix4f;

import java.util.EnumMap;
import java.util.Objects;

public class FourDirectionBlockDisplayElement extends UIElement {

    private final EnumMap<ColorDirection, Runnable> callbacks = new EnumMap<>(ColorDirection.class);
    private BlockState blockState = Blocks.FURNACE.defaultBlockState();
    private BlockEntity blockEntity = null;
    private float yRot0;

    private static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

    public FourDirectionBlockDisplayElement() {
        this.layout(l -> l.minHeight(110));
    }

    public FourDirectionBlockDisplayElement block(BlockState blockState, BlockEntity blockEntity) {
        this.blockState = blockState;
        this.blockEntity = blockEntity;
        return this;
    }

    public FourDirectionBlockDisplayElement yRot0(float yRot0) {
        this.yRot0 = yRot0;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawContents(GUIContext guiContext) {
        super.drawContents(guiContext);
        managedDraw(guiContext);
    }

    private void managedDraw(GUIContext guiContext) {
        float x = getPositionX();
        float y = getPositionY();
        float width = getContentWidth();
        float height = getContentHeight();
        float horizontalCenter = x + width / 2;
        float verticalCenter = y + height / 2;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        renderRing(
            guiContext.graphics,
            horizontalCenter,
            verticalCenter,
            0x77000000,
            -1.25f,
            32
        );
        renderRing(
            guiContext.graphics,
            horizontalCenter,
            verticalCenter,
            0xffededed,
            32.25f,
            33f
        );

        renderRotatedBlock(
            guiContext.pose.pose,
            blockState,
            horizontalCenter,
            verticalCenter,
            100,
            32f,
            Mth.clamp(guiContext.mc.cameraEntity.getXRot(), 38, 90),
            guiContext.mc.cameraEntity.getYRot(),
            6 / 16f,
            blockEntity.getBlockPos(),
            guiContext.mc
        );
    }

    public enum ColorDirection {
        RED, GREEN, BLUE, WHITE;

        public int color() {
            return switch (this) {
                case RED -> 0xfff35c5b;
                case GREEN -> 0xff5cf35b;
                case BLUE -> 0xff5cf3f2;
                case WHITE -> 0xfff3f3f2;
            };
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void renderRotatedBlock(
        PoseStack poseStack,
        BlockState block,
        float x,
        float y,
        float z,
        float scale,
        float xRot,
        float yRot,
        float blockVisualHeight,
        BlockPos blockPos,
        Minecraft minecraft
    ) {
        final float partialTick = minecraft.getTimer().getGameTimeDeltaPartialTick(true);
        poseStack.pushPose();
        poseStack.translate(-16, 16, 0);
        poseStack.translate(x, y, z);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(new Matrix4f().scaling(1, -1, 1));
        poseStack.translate(0.5f, blockVisualHeight, 0.5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot + 180f));
        poseStack.translate(-0.5f, 0, -0.5f);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        final FluidState fluidState = block.getFluidState();
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.setupGui3DDiffuseLighting(RenderSupport.L1, RenderSupport.L2);
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = blockRenderDispatcher.getBlockModel(block);
        for (RenderType renderType : model.getRenderTypes(block, RANDOM, ModelData.EMPTY)) {
            VertexConsumer bufferBuilder = buffers.getBuffer(renderType);
            blockRenderDispatcher.renderBatched(
                block,
                blockPos,
                ClientSupport.getFullBrightLevel(),
                poseStack,
                bufferBuilder,
                true,
                RANDOM,
                ModelData.EMPTY,
                renderType
            );
        }
        buffers.endLastBatch();
        if (!fluidState.isEmpty()) {
            if (block.getBlock() instanceof LiquidBlock) {
                block = block.setValue(LiquidBlock.LEVEL, block.getFluidState().getAmount());
            }
            blockRenderDispatcher.renderLiquid(
                blockPos,
                ClientSupport.getFullBrightLevel(),
                new VertexConsumerWithPose(
                    buffers.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidState)),
                    poseStack.last(),
                    BlockPos.ZERO
                ),
                block,
                fluidState
            );
            buffers.endLastBatch();
        }
        if (blockEntity != null && blockEntity.getBlockState().is(block.getBlock())) {
            BlockEntityRenderer<BlockEntity> renderer = minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
            if (renderer != null) {
                final Level originalLevel = blockEntity.getLevel();
                final BlockState originalBlockState = blockEntity.getBlockState();
                blockEntity.setBlockState(block);
                renderer.render(
                    blockEntity,
                    partialTick,
                    poseStack,
                    buffers,
                    LightTexture.FULL_BLOCK,
                    OverlayTexture.NO_OVERLAY
                );
                if (originalLevel != null) {
                    blockEntity.setLevel(originalLevel);
                }
                blockEntity.setBlockState(originalBlockState);
            }
        }
        poseStack.popPose();
    }

    public static void renderRing(
        GuiGraphics guiGraphics,
        float centerX,
        float centerY,
        int color,
        float innerDiameter,
        float outerDiameter
    ) {
        PoseStack poseStack = guiGraphics.pose();
        Matrix4f matrix4f = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(
            VertexFormat.Mode.QUADS,
            DefaultVertexFormat.POSITION_COLOR
        );
        float x1 = centerX - outerDiameter - 5;
        float y1 = centerY - outerDiameter - 5;
        float x2 = centerX + outerDiameter + 5;
        float y2 = centerY + outerDiameter + 5;
        bufferBuilder.addVertex(matrix4f, x1, y1, 10).setColor(color);
        bufferBuilder.addVertex(matrix4f, x1, y2, 10).setColor(color);
        bufferBuilder.addVertex(matrix4f, x2, y2, 10).setColor(color);
        bufferBuilder.addVertex(matrix4f, x2, y1, 10).setColor(color);

        Window window = Minecraft.getInstance().getWindow();
        float guiScale = (float) window.getGuiScale();
        RenderSystem.setShader(ModShaders::getRingShader);

        ModShaders.getRingShader()
            .safeGetUniform("Center")
            .set(centerX * guiScale, window.getHeight() - centerY * guiScale);
        ModShaders.getRingShader()
            .safeGetUniform("InnerDiameter")
            .set(innerDiameter * guiScale);
        ModShaders.getRingShader()
            .safeGetUniform("OuterDiameter")
            .set(outerDiameter * guiScale);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        BufferUploader.drawWithShader(Objects.requireNonNull(bufferBuilder.build()));
    }
}

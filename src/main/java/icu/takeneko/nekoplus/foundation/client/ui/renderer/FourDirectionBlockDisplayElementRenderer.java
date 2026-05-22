package icu.takeneko.nekoplus.foundation.client.ui.renderer;

import com.lowdragmc.lowdraglib2.gui.ui.UIElementRenderer;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.IGUIContext;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import dev.anvilcraft.lib.v2.rendering.gui.GuiRenderExtras;
import dev.anvilcraft.lib.v2.rendering.sdf.SdfGraphics;
import icu.takeneko.nekoplus.content.tile.logic.fpg.PinMode;
import icu.takeneko.nekoplus.foundation.ui.widgets.FourDirectionBlockDisplayElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;

public class FourDirectionBlockDisplayElementRenderer implements UIElementRenderer<FourDirectionBlockDisplayElement> {

    @Override
    public void drawBackgroundAdditional(FourDirectionBlockDisplayElement element, IGUIContext context) {
        UIElementRenderer.super.drawBackgroundAdditional(element, context);
        if (context instanceof GUIContext gc) {
            managedDraw(gc, element);
        }
    }

    private void managedDraw(GUIContext guiContext, FourDirectionBlockDisplayElement element) {
        float x = element.getPositionX();
        float y = element.getPositionY();
        float width = element.getContentWidth();
        float height = element.getContentHeight();
        float horizontalCenter = x + width / 2;
        float verticalCenter = y + height / 2;
        float yRot = guiContext.mc.getCameraEntity().getYRot();
        float xRot = Mth.clamp(guiContext.mc.getCameraEntity().getXRot(), 38, 90);

        element.updateYRot(yRot);
        SdfGraphics sdfGraphics = SdfGraphics.getInstance();
        GuiGraphicsExtractor graphics = guiContext.graphics;
        renderRing(
            sdfGraphics,
            horizontalCenter,
            verticalCenter,
            0x77000000,
            32,
            32f
        );

        renderRing(
            sdfGraphics,
            horizontalCenter,
            verticalCenter,
            0xffededed,
            33f,
            0.75f
        );

        element.getEntries().forEach((colorDirection, entry) -> {
            float rX = entry.position.x + horizontalCenter;
            float rY = entry.position.y + verticalCenter;
            renderRing(
                sdfGraphics,
                rX,
                rY,
                colorDirection.color() | 0xff000000,
                7f,
                7f
            );
        });

        sdfGraphics.draw(graphics);

        renderRotatedBlock(
            element.getBlockState(),
            horizontalCenter,
            verticalCenter,
            xRot,
            yRot,
            6 / 16f,
            graphics
        );

        Matrix3x2fStack poseStack = guiContext.pose.pose;
        poseStack.pushMatrix();
        poseStack.translate(horizontalCenter, verticalCenter);

        for (FourDirectionBlockDisplayElement.ColorDirection value : FourDirectionBlockDisplayElement.ColorDirection.values()) {
            FourDirectionBlockDisplayElement.DirectionEntry entry = element.getEntries().get(value);
            if (entry.ioState.getMode() == PinMode.DISABLE) continue;
            boolean isOutput = entry.ioState.getMode() == PinMode.OUTPUT;
            poseStack.pushMatrix();
            poseStack.rotate(-entry.rotationY);
            poseStack.translate(0, 33f);
            renderIOState(poseStack, isOutput, value.color() | 0xff000000);
            poseStack.popMatrix();
        }

        poseStack.popMatrix();
    }

    private void renderIOState(
        Matrix3x2fStack matrixStack,
        boolean isOutput,
        int color
    ) {
        Matrix3x2f matrix4f = new Matrix3x2f(matrixStack);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(
            VertexFormat.Mode.TRIANGLES,
            DefaultVertexFormat.POSITION_COLOR
        );
        if (isOutput) {
            bufferBuilder.addVertexWith2DPose(matrix4f, 0, 2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(matrix4f, 2.5f, -2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(matrix4f, -2.5f, -2.5f)
                .setColor(color);
        } else {
            bufferBuilder.addVertexWith2DPose(matrix4f, 0, -2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(matrix4f, -2.5f, 2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(matrix4f, 2.5f, 2.5f)
                .setColor(color);
        }
        MeshData meshData = bufferBuilder.buildOrThrow();
        RenderTypes.debugTriangleFan().draw(meshData);
    }

    private void renderRotatedBlock(
        BlockState block,
        float x,
        float y,
        float xRot,
        float yRot,
        float blockVisualHeight,
        GuiGraphicsExtractor guiGraphics
    ) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPose(new Matrix4f().scaling(1, -1, 1));
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.translate(0f, (float) -Math.cos(Math.toRadians(xRot)) * blockVisualHeight / 2, 0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot + 180f));
        poseStack.translate(-0.5f, 0, -0.5f);

        GuiRenderExtras.tessellateBlock(
            guiGraphics,
            block,
            null,
            null,
            x,
            y,
            32,
            true,
            poseStack
        );
        poseStack.popPose();
    }

    public static void renderRing(
        SdfGraphics sdfGraphics,
        float centerX,
        float centerY,
        int color,
        float radius,
        float strokeWidth
    ) {
        sdfGraphics
            .color(color)
            .center(true)
            .circle(centerX, centerY, radius)
            .stroke(strokeWidth);
    }
}

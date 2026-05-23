package icu.takeneko.nekoplus.foundation.client.ui.renderer.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import icu.takeneko.nekoplus.all.NPRenderPipelines;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

public record TinyTriangleRenderState(
    boolean isOutput,
    Matrix3x2f pose,
    int color,
    ScreenRectangle bounds
) implements GuiElementRenderState {
    @Override
    public void buildVertices(VertexConsumer bufferBuilder) {
        if (isOutput) {
            bufferBuilder.addVertexWith2DPose(pose, 0, 2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(pose, 2.5f, -2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(pose, -2.5f, -2.5f)
                .setColor(color);
        } else {
            bufferBuilder.addVertexWith2DPose(pose, 0, -2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(pose, -2.5f, 2.5f)
                .setColor(color);
            bufferBuilder.addVertexWith2DPose(pose, 2.5f, 2.5f)
                .setColor(color);
        }
    }

    @Override
    public RenderPipeline pipeline() {
        return NPRenderPipelines.POSITION_COLOR_TRIANGLES;
    }

    @Override
    public TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return null;
    }
}

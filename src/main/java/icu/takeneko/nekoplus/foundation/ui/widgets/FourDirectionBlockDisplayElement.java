package icu.takeneko.nekoplus.foundation.ui.widgets;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.IBindable;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.IBinding;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.IDataSource;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
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
import dev.dubhe.anvilcraft.util.MathUtil;
import dev.dubhe.anvilcraft.util.VertexConsumerWithPose;
import icu.takeneko.nekoplus.util.ClientSupport;
import it.unimi.dsi.fastutil.objects.Object2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.Objects;
import java.util.Optional;

public class FourDirectionBlockDisplayElement extends UIElement {

    private final EnumMap<ColorDirection, DirectionEntry> entries = new EnumMap<>(ColorDirection.class);
    private BlockState blockState = Blocks.FURNACE.defaultBlockState();
    private BlockEntity blockEntity = null;
    private float yRot0;

    private static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

    public FourDirectionBlockDisplayElement() {
        this.layout(l -> l.minHeight(110));
        for (ColorDirection value : ColorDirection.values()) {
            DirectionEntry entry = new DirectionEntry();
            entries.put(value, entry);
            addChild(entry.ioState);
        }
        updateYRot(0);
        addEventListener(UIEvents.MOUSE_DOWN, this::onMouseDown);
    }

    private void onMouseDown(UIEvent uiEvent) {
        float x = getPositionX();
        float y = getPositionY();
        float width = getContentWidth();
        float height = getContentHeight();
        float horizontalCenter = x + width / 2;
        float verticalCenter = y + height / 2;
        if (uiEvent.button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return;
        Vector2f position = new Vector2f(uiEvent.x, uiEvent.y);
        Optional<DirectionEntry> optional = entries.values().stream()
            .filter(it -> position.distance(it.position.x + horizontalCenter, it.position.y + verticalCenter) <= 10).findFirst();
        optional.ifPresent(entry -> entry.callback.run());
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

    public FourDirectionBlockDisplayElement bindIOState(ColorDirection direction, @Nullable IBinding<Boolean> binding) {
        entries.get(direction).ioState.bind(binding);
        return this;
    }

    public FourDirectionBlockDisplayElement setOnClickListener(ColorDirection direction, Runnable callback) {
        entries.get(direction).callback = callback;
        return this;
    }

    private void updateYRot(float clientRotation) {
        for (ColorDirection value : ColorDirection.values()) {
            float calculated = value.defaultYRot() + yRot0 + clientRotation;
            entries.get(value).rotationY = calculated;
            entries.get(value).position = MathUtil.rotationDegrees(new Vector2f(0, 45), -calculated);
        }
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
        float yRot = guiContext.mc.cameraEntity.getYRot();
        float xRot = Mth.clamp(guiContext.mc.cameraEntity.getXRot(), 38, 90);

        updateYRot(yRot);

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
            xRot,
            yRot,
            6 / 16f,
            blockEntity.getBlockPos(),
            guiContext.mc
        );
// test
//        var gap = 32;
//        var numPerLine = 19;
//        var scale = 24f;
//        for (int iy = 0; iy <= 45/5; iy++) {
//            for (int ix = 0; ix <= 90 / 5; ix++) {
//                var rotX = 90 - ix * 5;
//                var rotY = iy * 5;
//                float x1 = gap + ix % numPerLine * gap;
//                float y1 = gap + iy * gap;
//                renderRotatedBlock(
//                    guiContext.pose.pose,
//                    blockState,
//                    x1,
//                    y1,
//                    100,
//                    scale,
//                    rotX,
//                    rotY,
//                    6 / 16f,
//                    blockEntity.getBlockPos(),
//                    guiContext.mc
//                );
//                renderRotatedBlock(
//                    guiContext.pose.pose,
//                    blockState,
//                    x1,
//                    y1,
//                    100,
//                    scale,
//                    -90,
//                    0,
//                    6 / 16f,
//                    blockEntity.getBlockPos(),
//                    guiContext.mc
//                );
//            }
//        }

        RenderSystem.enableBlend();

        entries.forEach((colorDirection, entry) -> {
            float rX = entry.position.x + horizontalCenter;
            float rY = entry.position.y + verticalCenter;
            renderRing(
                guiContext.graphics,
                rX,
                rY,
                colorDirection.color() | 0xff000000,
                -1.25f,
                7
            );
        });
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
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.translate(0f, (float) -Math.cos(Math.toRadians(xRot)) * blockVisualHeight / 2, 0f);
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

        ShaderInstance shader = ModShaders.getRingShader();
        shader.safeGetUniform("Center").set(centerX * guiScale, window.getHeight() - centerY * guiScale);
        shader.safeGetUniform("InnerDiameter").set(innerDiameter * guiScale);
        shader.safeGetUniform("OuterDiameter").set(outerDiameter * guiScale);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        BufferUploader.drawWithShader(Objects.requireNonNull(bufferBuilder.build()));
    }

    public enum ColorDirection {
        RED, GREEN, BLUE, WHITE;

        public int color() {
            return switch (this) {
                case RED -> 0xf35c5b;
                case GREEN -> 0x5cf35b;
                case BLUE -> 0x5cf3f2;
                case WHITE -> 0xf3f3f2;
            };
        }

        public int defaultYRot() {
            return switch (this) {
                case RED -> 90;
                case GREEN -> 180;
                case BLUE -> 270;
                case WHITE -> 0;
            };
        }
    }

    private static class BoundIODirectionUIStub extends UIElement implements IBindable<Boolean> {

        private boolean isOutput = false;

        @Override
        public Boolean getValue() {
            return isOutput;
        }

        @Override
        public IDataSource<Boolean> setValue(@Nullable Boolean value) {
            if (value == null) {
                isOutput = false;
                return this;
            }
            isOutput = value;
            return this;
        }
    }

    private static class DirectionEntry {
        public float rotationY = 0;
        public Runnable callback = DirectionEntry::emptyCallback;
        public Vector2f position = new Vector2f(0, 42);
        public final BoundIODirectionUIStub ioState = new BoundIODirectionUIStub();

        private static void emptyCallback() {
        }
    }
}

package icu.takeneko.nekoplus.client.renderer.tesr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import icu.takeneko.nekoplus.client.renderer.helper.AnimationDataConfigurator;
import io.github.tt432.eyelib.Eyelib;
import io.github.tt432.eyelib.capability.EyelibAttachableData;
import io.github.tt432.eyelib.capability.RenderData;
import io.github.tt432.eyelib.capability.component.AnimationComponent;
import io.github.tt432.eyelib.capability.component.ClientEntityComponent;
import io.github.tt432.eyelib.capability.component.ModelComponent;
import io.github.tt432.eyelib.client.ClientTickHandler;
import io.github.tt432.eyelib.client.animation.AnimationEffects;
import io.github.tt432.eyelib.client.animation.BrAnimator;
import io.github.tt432.eyelib.client.animation.RuntimeParticlePlayData;
import io.github.tt432.eyelib.client.entity.BrClientEntity;
import io.github.tt432.eyelib.client.model.Model;
import io.github.tt432.eyelib.client.render.RenderHelper;
import io.github.tt432.eyelib.client.render.RenderParams;
import io.github.tt432.eyelib.client.render.bone.BoneRenderInfos;
import io.github.tt432.eyelib.client.render.controller.RenderControllerEntry;
import io.github.tt432.eyelib.molang.MolangScope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public abstract class EyelibBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererProvider.Context context;

    EyelibBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(
        T blockEntity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        int packedOverlay
    ) {
        RenderData<T> data = getData(blockEntity);
        configureRenderData(data, blockEntity);
        MolangScope scope = data.getScope();
        AnimationDataConfigurator configurator = blockEntity instanceof AnimationDataConfigurator c ? c : AnimationDataConfigurator.DEFAULT;
        configureMolangScope(scope, configurator, partialTick);
        ClientEntityComponent clientEntityComponent = data.getClientEntityComponent();
        AnimationComponent animationComponent = data.getAnimationComponent();
        List<ModelComponent> components = setupClientEntity(blockEntity, clientEntityComponent, data);
        AnimationEffects effects = new AnimationEffects();

        BoneRenderInfos tickedInfos;
        if (animationComponent.getSerializableInfo() != null) {
            tickedInfos = BrAnimator.tickAnimation(
                animationComponent,
                scope,
                effects,
                (ClientTickHandler.getTick() + partialTick) / 20,
                () -> clientEntityComponent.getClientEntity()
                    .scripts()
                    .ifPresent(scripts -> scripts.pre_animation().eval(scope))
            );
        } else {
            tickedInfos = BoneRenderInfos.EMPTY;
        }
        renderComponents(
            bufferSource,
            poseStack,
            packedLight,
            packedOverlay,
            partialTick,
            blockEntity,
            data,
            components,
            tickedInfos,
            effects,
            c -> {
            }
        );
    }

    private RenderData<T> getData(T be) {
        RenderData<?> value = be.getExistingDataOrNull(EyelibAttachableData.RENDER_DATA);
        if (value != null) {
            //noinspection unchecked
            return (RenderData<T>) value;
        }

        value = new RenderData<>();
        be.setData(EyelibAttachableData.RENDER_DATA.get(), (RenderData<Object>) value);
        //noinspection unchecked
        return (RenderData<T>) value;
    }

    private void configureMolangScope(MolangScope scope, AnimationDataConfigurator configurator, float partialTick) {
        scope.set("variable.partial_tick", partialTick);
        scope.set("variable.animation_speed", configurator.getAnimationRateMultiplier());
        configurator.configureMolangScope(scope);
    }

    private void configureRenderData(RenderData<T> data, T blockEntity) {
        if (data.getOwner() != blockEntity) {
            data.init(blockEntity);
        }
    }

    public static @NotNull List<ModelComponent> setupClientEntity(BlockEntity entity, ClientEntityComponent clientEntityComponent, RenderData<?> cap) {
        BrClientEntity clientEntity = Eyelib.getAttachableLoader().get(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType()));

        BrClientEntity oldEntity = clientEntityComponent.getClientEntity();

        boolean changed = false;

        if (clientEntity != null) {
            if (oldEntity == null || !oldEntity.identifier().equals(clientEntity.identifier())) {
                clientEntityComponent.setClientEntity(clientEntity);
                changed = true;
            }
        }

        List<ModelComponent> components = cap.getModelComponents();

        if (clientEntityComponent.getClientEntity() != null) {
            components.clear();
            BrClientEntity ce = clientEntityComponent.getClientEntity();

            for (String renderController : ce.render_controllers()) {
                RenderControllerEntry renderControllerEntry = Eyelib.getRenderControllerManager().get(renderController);
                if (renderControllerEntry != null)
                    components.add(renderControllerEntry.setupModel(cap.getScope(), clientEntityComponent.getClientEntity()));
            }

            if (changed) {
                ce.scripts().ifPresent(s -> {
                    s.initialize().eval(cap.getScope());
                    s.pre_animation().eval(cap.getScope());
                });
            }

            ce.scripts().ifPresent(s -> cap.getAnimationComponent().setup(ce.animations(), s.animate()));

            cap.getScope().getOwner().replace(BrClientEntity.class, ce);
        } else {
            cap.getScope().getOwner().remove(BrClientEntity.class);
        }

        return components;
    }

    public static <T> boolean renderComponents(
        MultiBufferSource multiBufferSource,
        PoseStack poseStack,
        int packedLight,
        int overlay,
        float partialTick,
        @Nullable BlockEntity entity,
        RenderData<T> cap,
        List<ModelComponent> components,
        BoneRenderInfos tickedInfos,
        AnimationEffects effects,
        Consumer<RenderHelper> consumer
    ) {
        AtomicBoolean rendered = new AtomicBoolean(false);
        ClientEntityComponent clientEntityComponent = cap.getClientEntityComponent();
        components.forEach(modelComponent -> {
            Model model = modelComponent.getModel();
            ResourceLocation texture = modelComponent.getTexture();

            if (model != null && texture != null) {
                rendered.set(true);
                RenderType renderType = modelComponent.getRenderType(texture);
                VertexConsumer buffer = multiBufferSource.getBuffer(renderType);

                poseStack.pushPose();

                RenderParams renderParams = new RenderParams(
                    null,
                    poseStack.last().copy(),
                    poseStack,
                    renderType,
                    texture,
                    modelComponent.isSolid(),
                    buffer,
                    packedLight,
                    overlay,
                    modelComponent.getPartVisibility()
                );

                if (clientEntityComponent.getClientEntity() != null) {
                    clientEntityComponent.getClientEntity().scripts().ifPresent(s -> {
                        MolangScope scope = cap.getScope();
                        poseStack.scale(s.getScaleX(scope), s.getScaleY(scope), s.getScaleZ(scope));
                    });
                }

                {
                    RenderHelper renderHelper = Eyelib.getRenderHelper();
//                    if (canUseHighSpeedRender() && buffer instanceof LazyComputeBufferBuilder lazy) {
//                        var helper = helpers.computeIfAbsent(renderType, r -> Pair.of(new VertexComputeHelper(), multiBufferSource));
//                        lazy.setEyelib$helper(helper.left());
//                    }
                    renderHelper.render(renderParams, model, tickedInfos);

                    renderHelper.collectLocators(model, tickedInfos);
                    Map<String, Matrix4f> locators = renderHelper.getContext().get("locators");

                    if (locators != null) {
                        for (List<RuntimeParticlePlayData> particle : effects.particles) {
                            for (RuntimeParticlePlayData data : particle) {
                                data.emitter().initPose(locators.get(data.locator()), null);
                            }
                        }

                        consumer.accept(renderHelper);
                    }
                }

                ResourceLocation emissiveTexture = texture.withPath(s -> replacePng(s, ".png", ".emissive.png"));
                AbstractTexture texture1 = Minecraft.getInstance().getTextureManager().getTexture(emissiveTexture);

                if (texture1 != MissingTextureAtlasSprite.getTexture()) {
                    RenderType rt1 = modelComponent.getRenderType(emissiveTexture);
                    VertexConsumer buffer1 = multiBufferSource.getBuffer(rt1);
                    RenderHelper renderHelper = Eyelib.getRenderHelper();

//                    if (canUseHighSpeedRender() && buffer1 instanceof LazyComputeBufferBuilder lazy) {
//                        var helper = helpers.computeIfAbsent(renderType, r -> Pair.of(new VertexComputeHelper(), multiBufferSource));
//                        lazy.setEyelib$helper(helper.left());
//                    }
                    renderHelper.render(
                        renderParams
                            .withRenderType(rt1)
                            .withLight(LightTexture.FULL_BRIGHT)
                            .withConsumer(buffer1)
                            .withTexture(emissiveTexture),
                        model, tickedInfos);
                }

                poseStack.popPose();
            }
        });

        return rendered.get();
    }

    static String replacePng(String originalString, String old, String newStr) {
        int lastIndexOfDot = originalString.lastIndexOf(old);

        if (lastIndexOfDot != -1) {
            String beforeDot = originalString.substring(0, lastIndexOfDot);
            return beforeDot + newStr;
        } else {
            return originalString;
        }
    }
}

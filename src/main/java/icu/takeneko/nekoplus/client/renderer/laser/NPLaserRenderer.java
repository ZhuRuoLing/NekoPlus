package icu.takeneko.nekoplus.client.renderer.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.api.rendering.CacheableBlockEntityRenderer;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;

public class NPLaserRenderer implements CacheableBlockEntityRenderer<BaseLaserBlockEntity> {

    @Override
    public void render(
        BaseLaserBlockEntity blockEntity,
        MultiBufferSource.BufferSource buffer,
        PoseStack poseStack
    ) {
        NPLaserState laserState = NPLaserState.create(blockEntity, poseStack);
        if (laserState != null) {
            NPLaserCompiler.compile(
                laserState,
                buffer::getBuffer
            );
        }
    }
}

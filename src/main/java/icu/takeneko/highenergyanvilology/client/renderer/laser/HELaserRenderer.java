package icu.takeneko.highenergyanvilology.client.renderer.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.api.rendering.CacheableBlockEntityRenderer;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;

public class HELaserRenderer implements CacheableBlockEntityRenderer<BaseLaserBlockEntity> {

    @Override
    public void render(
        BaseLaserBlockEntity blockEntity,
        MultiBufferSource.BufferSource buffer,
        PoseStack poseStack
    ) {
        HELaserState laserState = HELaserState.create(blockEntity, poseStack);
        if (laserState != null) {
            HELaserCompiler.compile(
                laserState,
                buffer::getBuffer
            );
        }
    }
}

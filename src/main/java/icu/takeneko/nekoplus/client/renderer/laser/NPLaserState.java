package icu.takeneko.nekoplus.client.renderer.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserState;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import org.jetbrains.annotations.Nullable;

public record NPLaserState(
    LaserState parent,
    int color,
    boolean thinner
) {
    public static @Nullable NPLaserState create(BaseLaserBlockEntity blockEntity, PoseStack poseStack) {
        LaserState parent = LaserState.create(blockEntity, poseStack);
        if (parent == null) {
            return null;
        }
        boolean thinner = false;
        int color = 0xff1313;
        if (blockEntity instanceof HighEnergyLaserBlockEntity || LaserRendererInternals.hasPureHELaserSource(blockEntity)) {
            color = 0x8000ff;
            thinner = true;
        }
        return new NPLaserState(parent, color, thinner);
    }
}

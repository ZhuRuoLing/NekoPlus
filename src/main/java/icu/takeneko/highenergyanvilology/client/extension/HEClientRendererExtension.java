package icu.takeneko.highenergyanvilology.client.extension;

import lombok.Getter;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class HEClientRendererExtension implements IClientItemExtensions {
    @Getter
    private final BlockEntityWithoutLevelRenderer customRenderer;

    public HEClientRendererExtension(BlockEntityWithoutLevelRenderer customRenderer) {
        this.customRenderer = customRenderer;
    }
}

package icu.takeneko.nekoplus.client.extension;

import lombok.Getter;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class NPClientRendererExtension implements IClientItemExtensions {
    @Getter
    private final BlockEntityWithoutLevelRenderer customRenderer;

    public NPClientRendererExtension(BlockEntityWithoutLevelRenderer customRenderer) {
        this.customRenderer = customRenderer;
    }
}

package icu.takeneko.nekoplus.client.extension;

import icu.takeneko.nekoplus.client.renderer.bewlr.EyelibBlockItemBEWLR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;

public class NPClientExtension implements IClientItemExtensions {
    private final EyelibBlockItemBEWLR bewlr;

    public NPClientExtension(List<Item> bewlrItem) {
        bewlr = new EyelibBlockItemBEWLR(
            Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels(),
            bewlrItem
        );
    }


    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return bewlr;
    }
}
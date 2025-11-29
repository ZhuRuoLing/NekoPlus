package icu.takeneko.highenergyanvilology.client;

import icu.takeneko.highenergyanvilology.client.renderer.bewlr.EyelibBlockItemBEWLR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;

public class HEClientExtension implements IClientItemExtensions {
    private final EyelibBlockItemBEWLR bewlr;

    public HEClientExtension(List<Item> bewlrItem) {
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
package icu.takeneko.nekoplus.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class TooltipUtils {
    public static Component indentListHeader(Component content) {
        return Component.translatable("tooltip.format.indent_list", content);
    }

    public static Component indent(Component content) {
        return Component.translatable("tooltip.format.indent", content);
    }

    public static MutableComponent atlasSprite(Identifier atlas, Identifier sprite) {
        return Component.object(new AtlasSprite(atlas, sprite));
    }

    public static MutableComponent itemAtlasSprite(Holder<Item> item, int padding) {
        return atlasSprite(AtlasIds.ITEMS, item.getKey().identifier().withPrefix("item/")).append(" ".repeat(padding));
    }

    public static MutableComponent itemAtlasSprite(Holder<Item> item) {
        return atlasSprite(AtlasIds.ITEMS, item.getKey().identifier().withPrefix("item/"));
    }

    public static MutableComponent itemAtlasSprite(Item item) {
        return atlasSprite(AtlasIds.ITEMS, BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"));
    }
}

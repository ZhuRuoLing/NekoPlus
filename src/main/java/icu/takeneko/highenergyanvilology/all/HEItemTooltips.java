package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class HEItemTooltips {

    private static final Map<Item, Component> tooltips = new HashMap<>();

    private static void tooltip(ItemLike item, String translation) {
        tooltips.put(item.asItem(), translatable(BuiltInRegistries.ITEM.getKey(item.asItem()).toLanguageKey("tooltip"), translation));
    }

    private static void tooltip(ItemLike item, String key, String translation) {
        tooltips.put(item.asItem(), translatable(key, translation));
    }

    private static Component translatable(String key, String translation) {
        return HEAnvilology.REGISTRATE.addRawLang(key, translation);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void on(ItemTooltipEvent event) {
        Component component = tooltips.get(event.getItemStack().getItem());
        if (component == null) return;
        event.getToolTip().add(1, component.copy().withStyle(ChatFormatting.GRAY));
    }

    public static void setupTooltips() {
        tooltip(HEBlocks.PARTICLE_STABILIZER, "That`s cold enough.");
    }
}

package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
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
public class NPItemTooltips {

    private static final Map<Item, Component> tooltips = new HashMap<>();

    private static void tooltip(ItemLike item, String translation) {
        tooltips.put(item.asItem(), translatable(BuiltInRegistries.ITEM.getKey(item.asItem()).toLanguageKey("tooltip"), translation));
    }

    private static void tooltip(ItemLike item, String key, String translation) {
        tooltips.put(item.asItem(), translatable(key, translation));
    }

    private static Component translatable(String key, String translation) {
        return NekoPlus.REGISTRUM.addRawLang(key, translation);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void on(ItemTooltipEvent event) {
        Component component = tooltips.get(event.getItemStack().getItem());
        if (component == null) return;
        event.getToolTip().add(1, component.copy().withStyle(ChatFormatting.GRAY));
    }

    public static void setupTooltips() {
        tooltip(NPBlocks.PARTICLE_STABILIZER, "That`s cold enough.");
        tooltip(NPBlocks.HIGH_ENERGY_LASER, "One beats sixty-four");
        tooltip(NPBlocks.ROYAL_STEEL_CASING, "§7All you need is §5I§dm§4a§cg§ei§an§ba§3t§7i§1o§5n§7");

        tooltip(NPItems.STABILIZE_POWDER, "S₈((Al₆Si₆Ca₈Na₈)₁₂(Al₃Si₃Na₄Cl)₂(FeS₂)(CaCO₃))₂(Si(FeS₂)₅(CrAl₂O₃)Hg₃)₃Lv₅");
        tooltip(NPItems.CHARGED_LEVITATION_POWDER, "Lv");
        tooltip(NPItems.DRY_ICE, "CO₂");
    }
}

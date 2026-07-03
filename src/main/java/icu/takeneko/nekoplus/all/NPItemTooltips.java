package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import icu.takeneko.nekoplus.util.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class NPItemTooltips {

    private static final Map<Item, Component> tooltips = new HashMap<>();

    private static void tooltip(ItemLike item, String translation) {
        tooltips.put(
            item.asItem(),
            translatable(BuiltInRegistries.ITEM.getKey(item.asItem()).toLanguageKey("tooltip"), translation)
        );
    }

    private static void tooltip(ItemLike item, String key, String translation) {
        tooltips.put(item.asItem(), translatable(key, translation));
    }

    private static Component translatable(String key, String translation) {
        return NekoPlus.REGISTRUM.addRawLang(key, translation);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void on(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        Component component = tooltips.get(itemStack.getItem());
        if (component == null) return;
        event.getToolTip().add(1, component.copy().withStyle(ChatFormatting.GRAY));
    }

    @SubscribeEvent
    public static void onEnhancementModule(AddAttributeTooltipsEvent event) {
        ItemStack itemStack = event.getStack();
        List<Component> components = new ArrayList<>(1);
        if (itemStack.has(NPDataComponents.ENHANCEMENT_MODULE)) {
            List<NPEnhancementModule> modules = itemStack.get(NPDataComponents.ENHANCEMENT_MODULE);
            if (modules.isEmpty()) {
                components.add(
                    TooltipUtils.itemAtlasSprite(NPItems.ADVANCED_PROCESSOR, 1)
                        .append(
                            Component.translatable("tooltip.nekoplus.enhancement_module.no_module")
                                .withStyle(ChatFormatting.GRAY)
                        )
                );
            } else {
                components.add(
                    TooltipUtils.itemAtlasSprite(NPItems.ADVANCED_PROCESSOR, 1)
                        .append(
                            Component.translatable("tooltip.nekoplus.enhancement_module.enhancement_modules")
                                .withStyle(ChatFormatting.DARK_AQUA)
                        )
                );
                if (modules.size() > 3) {
                    Map<NPEnhancementModule, Integer> moduleCounts = new LinkedHashMap<>();
                    for (NPEnhancementModule module : modules) {
                        moduleCounts.merge(module, 1, Integer::sum);
                    }
                    for (Map.Entry<NPEnhancementModule, Integer> entry : moduleCounts.entrySet()) {
                        addEnhancementModuleTooltip(components, entry.getKey(), entry.getValue());
                    }
                } else {
                    for (NPEnhancementModule module : modules) {
                        addEnhancementModuleTooltip(components, module, 1);
                    }
                }
            }
        }
        components.forEach(event::addTooltipLines);
    }

    private static void addEnhancementModuleTooltip(
        List<Component> components,
        NPEnhancementModule module,
        int count
    ) {
        MutableComponent header = TooltipUtils.itemAtlasSprite(module.getType().itemHolder(), 1)
            .append(module.name().copy().withStyle(ChatFormatting.GOLD));
        if (count > 1) {
            header.append(Component.literal(" x" + count).withStyle(ChatFormatting.GRAY));
        }
        components.add(TooltipUtils.indentListHeader(header));
        for (Component component : module.tooltip()) {
            components.add(TooltipUtils.indent(component.copy().withStyle(ChatFormatting.GRAY)));
        }
    }

    public static void setupTooltips() {
        tooltip(NPBlocks.PARTICLE_STABILIZER, "That`s cold enough.");
        tooltip(NPBlocks.HIGH_ENERGY_LASER, "One beats sixty-four");
        tooltip(NPBlocks.ROYAL_STEEL_CASING, "§7All you need is §5I§dm§4a§cg§ei§an§ba§3t§7i§1o§5n§7");
        tooltip(NPBlocks.SHULKER_HATCH, "Make a nested shulker box behaves like drawer");
        tooltip(NPBlocks.CAT_ANVIL, "*Meow*");
        tooltip(NPBlocks.BATTERY, "Filter Capacitor");
        tooltip(NPBlocks.HUGE_BATTERY, "FILTER CAPACITOR");


        tooltip(
            NPItems.STABILIZE_POWDER,
            "S₈((Al₆Si₆Ca₈Na₈)₁₂(Al₃Si₃Na₄Cl)₂(FeS₂)(CaCO₃))₂(Si(FeS₂)₅(CrAl₂O₃)Hg₃)₃Lv₅"
        );
        tooltip(NPItems.CHARGED_LEVITATION_POWDER, "Lv");
        tooltip(NPItems.DRY_ICE, "CO₂");
        tooltip(NPItems.ADVANCED_PROCESSOR, "*Intel Jingles*");

        tooltip(NPItems.ANTI_GRAVITY_MODULE, "Grants creative flight and removes flying mining penalty.");
        tooltip(NPItems.TITANIUM_CRYSTAL_MODULE, "Ti-Fe alloys — treated via controlled heat and amorphization — shed brittleness without sacrificing strength.");
        tooltip(NPItems.EXOSKELETAL_LEG_FRAME_MODULE, "Increases movement speed by 20% per module, step height by 0.5, jump height by 30%");
        tooltip(NPItems.HOLOGRAM_PROJECTOR_MODULE, "Projects additional contextual information as a holographic overlay.");
    }
}

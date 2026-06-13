package icu.takeneko.nekoplus.item;

import icu.takeneko.nekoplus.NekoPlus;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class EnhancementSmithingTemplate extends SmithingTemplateItem {
    private static final Identifier EMPTY_SLOT_HELMET = Identifier.withDefaultNamespace("container/slot/helmet");
    private static final Identifier EMPTY_SLOT_CHESTPLATE = Identifier.withDefaultNamespace("container/slot/chestplate");
    private static final Identifier EMPTY_SLOT_LEGGINGS = Identifier.withDefaultNamespace("container/slot/leggings");
    private static final Identifier EMPTY_SLOT_BOOTS = Identifier.withDefaultNamespace("container/slot/boots");
    private static final Identifier EMPTY_SLOT_HOE = Identifier.withDefaultNamespace("container/slot/hoe");
    private static final Identifier EMPTY_SLOT_AXE = Identifier.withDefaultNamespace("container/slot/axe");
    private static final Identifier EMPTY_SLOT_SWORD = Identifier.withDefaultNamespace("container/slot/sword");
    private static final Identifier EMPTY_SLOT_SHOVEL = Identifier.withDefaultNamespace("container/slot/shovel");
    private static final Identifier EMPTY_SLOT_SPEAR = Identifier.withDefaultNamespace("container/slot/spear");
    private static final Identifier EMPTY_SLOT_PICKAXE = Identifier.withDefaultNamespace("container/slot/pickaxe");
    public EnhancementSmithingTemplate(Properties properties) {
        super(
            Component.translatable("tooltip.nekoplus.enhancement_smithing_template.applies_to"),
            Component.translatable("item.nekoplus.advanced_processor"),
            Component.translatable("tooltip.nekoplus.enhancement_smithing_template.base_slot_description"),
            Component.translatable("tooltip.nekoplus.enhancement_smithing_template.additions_slot_description"),
            List.of(
                EMPTY_SLOT_HELMET,
                EMPTY_SLOT_CHESTPLATE,
                EMPTY_SLOT_LEGGINGS,
                EMPTY_SLOT_BOOTS,
                EMPTY_SLOT_HOE,
                EMPTY_SLOT_AXE,
                EMPTY_SLOT_SWORD,
                EMPTY_SLOT_SHOVEL,
                EMPTY_SLOT_SPEAR,
                EMPTY_SLOT_PICKAXE
            ),
            List.of(
                NekoPlus.location("container/slot/chip")
            ),
            properties
        );
    }
}

package icu.takeneko.nekoplus.item;

import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import icu.takeneko.nekoplus.foundation.item.module.type.NPEnhancementModuleType;
import icu.takeneko.nekoplus.util.TooltipUtils;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class EnhancementModuleItem<T extends NPEnhancementModule> extends Item {
    @Getter
    private final NPEnhancementModuleType<T> moduleType;

    public EnhancementModuleItem(Properties properties, NPEnhancementModuleType<T> moduleType) {
        super(properties);
        this.moduleType = moduleType;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(
        ItemStack itemStack,
        TooltipContext context,
        TooltipDisplay display,
        Consumer<Component> builder,
        TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("item.minecraft.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
        List<MutableComponent> components = moduleType.applicableItemSlots().stream()
            .map(it -> Component.translatable("tooltip.nekoplus.enhancement_module.slot." + it.name().toLowerCase(Locale.ROOT)))
            .map(it -> it.withStyle(ChatFormatting.BLUE))
            .toList();

        Component component = TooltipUtils.indent(
            ComponentUtils.formatList(
                components,
                Component.literal(", ")
                    .withStyle(ChatFormatting.GRAY)
            )
        );
        builder.accept(component);
    }
}

package icu.takeneko.nekoplus.util;

import net.minecraft.network.chat.Component;

public class TooltipUtils {
    public static Component indentList(Component content) {
        return Component.translatable("tooltip.format.indent_list", content);
    }

    public static Component indent(Component content) {
        return Component.translatable("tooltip.format.indent", content);
    }
}

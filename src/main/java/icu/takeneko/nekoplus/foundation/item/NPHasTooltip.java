package icu.takeneko.nekoplus.foundation.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface NPHasTooltip {

    boolean hasDetailTooltip(ItemStack itemStack);

    boolean hasInformationTooltip(ItemStack itemStack);

    /// Detail description tooltip on Shift key
    boolean onDetailTooltip(ItemStack itemStack, List<Component> tooltips);

    /// Information tooltip on ctrl key, e.g. block entity states saved into block item
    boolean onInformationTooltip(ItemStack itemStack, List<Component> tooltips);
}

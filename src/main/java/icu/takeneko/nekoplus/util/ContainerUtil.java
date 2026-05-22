package icu.takeneko.nekoplus.util;

import net.minecraft.world.item.ItemStack;

public class ContainerUtil {
    public static ItemStack insertItem(IItemHandler itemHandler, ItemStack input) {
        ItemStack retain = input;
        for (int i = 0; i < itemHandler.getSlots() && !retain.isEmpty(); i++) {
            ItemStack result = itemHandler.insertItem(i, retain, false);
            if (result.getCount() != retain.getCount()) {
                retain = itemHandler.insertItem(i, retain, true);
            }
        }
        return retain;
    }
}

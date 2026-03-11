package icu.takeneko.nekoplus.foundation.inventory;

import net.minecraft.world.item.ItemStack;

public interface NPItemHandlerOwner {
    void onContentChanged();

    boolean isItemValid(int slot, ItemStack stack);

    NPItemHandler getItemHandler();
}

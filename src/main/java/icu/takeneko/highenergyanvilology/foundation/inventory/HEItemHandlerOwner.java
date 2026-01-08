package icu.takeneko.highenergyanvilology.foundation.inventory;

import net.minecraft.world.item.ItemStack;

public interface HEItemHandlerOwner {
    void onContentChanged();

    boolean isItemValid(int slot, ItemStack stack);

    HEItemHandler getItemHandler();
}

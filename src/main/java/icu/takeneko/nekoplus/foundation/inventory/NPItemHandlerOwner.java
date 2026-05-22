package icu.takeneko.nekoplus.foundation.inventory;

import net.neoforged.neoforge.transfer.item.ItemResource;

public interface NPItemHandlerOwner {
    void onContentChanged();

    boolean isItemValid(int slot, ItemResource stack);

    NPItemHandler getItemHandler();
}

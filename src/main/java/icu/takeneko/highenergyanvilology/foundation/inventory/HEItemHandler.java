package icu.takeneko.highenergyanvilology.foundation.inventory;

import net.neoforged.neoforge.items.ItemStackHandler;

public class HEItemHandler extends ItemStackHandler {
    private final ItemHandlerOwner owner;

    public HEItemHandler(int size, ItemHandlerOwner owner) {
        super(size);
        this.owner = owner;
    }

    public HEItemHandler(ItemHandlerOwner owner) {
        this.owner = owner;
    }

    @Override
    protected void onContentsChanged(int slot) {
        owner.onContentChanged();
    }
}

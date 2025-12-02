package icu.takeneko.highenergyanvilology.foundation.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
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

    public NonNullList<ItemStack> getStacks() {
        return stacks;
    }
}

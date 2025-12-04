package icu.takeneko.highenergyanvilology.foundation.inventory;

import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class HEItemHandler extends ItemStackHandler implements IContentChangeAware {
    private final ItemHandlerOwner owner;

    @Getter
    @Setter
    private Runnable onContentsChanged;

    public HEItemHandler(int size, ItemHandlerOwner owner) {
        super(size);
        this.owner = owner;
    }

    public HEItemHandler(ItemHandlerOwner owner) {
        this.owner = owner;
    }

    @Override
    protected void onContentsChanged(int slot) {
        onContentsChanged.run();
        owner.onContentChanged();
    }

    public NonNullList<ItemStack> getStacks() {
        return stacks;
    }
}

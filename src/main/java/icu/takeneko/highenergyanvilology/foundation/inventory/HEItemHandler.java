package icu.takeneko.highenergyanvilology.foundation.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class HEItemHandler extends ItemStackHandler implements IContentChangeAware {
    private final ItemHandlerOwner owner;
    private final Table<Integer, Integer, HEItemHandlerSlice> cache = HashBasedTable.create();

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
        if (onContentsChanged != null) {
            onContentsChanged.run();
        }
        owner.onContentChanged();
    }

    public NonNullList<ItemStack> getStacks() {
        return stacks;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return owner.isItemValid(slot, stack);
    }

    public HEItemHandlerSlice slice(int start, int end) {
        return slice(start, end, true);
    }

    /**
     * Returns a {@link HEItemHandlerSlice} from specified index <code>start</code> , inclusive , to specified index <code>end</code> , exclusive.
     */
    public HEItemHandlerSlice slice(int start, int end, boolean enableInput) {
        Preconditions.checkArgument(start < end);
        Preconditions.checkElementIndex(start, stacks.size());
        Preconditions.checkElementIndex(end - 1, stacks.size());
        HEItemHandlerSlice slice = cache.get(start, end);
        if (slice == null) {
            slice = HEItemHandlerSlice.of(this, start, end);
            cache.put(start, end, slice);
        }
        slice.setEnableInput(enableInput);
        return slice;
    }
}

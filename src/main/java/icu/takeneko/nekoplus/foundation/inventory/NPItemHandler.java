package icu.takeneko.nekoplus.foundation.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.lowdragmc.lowdraglib2.syncdata.IContentChangeAware;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class NPItemHandler extends ItemStackHandler implements IContentChangeAware {
    private final NPItemHandlerOwner owner;
    private final Table<Integer, Integer, NPItemHandlerSlice> cache = HashBasedTable.create();

    @Getter
    @Setter
    private Runnable onContentsChanged;

    public NPItemHandler(int size, NPItemHandlerOwner owner) {
        super(size);
        this.owner = owner;
    }

    public NPItemHandler(NPItemHandlerOwner owner) {
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

    public NPItemHandlerSlice slice(int start, int end) {
        return slice(start, end, true);
    }

    /**
     * Returns a {@link NPItemHandlerSlice} from specified index <code>start</code> , inclusive , to specified index <code>end</code> , exclusive.
     */
    public NPItemHandlerSlice slice(int start, int end, boolean enableInput) {
        Preconditions.checkArgument(start < end);
        Preconditions.checkElementIndex(start, stacks.size());
        Preconditions.checkElementIndex(end - 1, stacks.size());
        NPItemHandlerSlice slice = cache.get(start, end);
        if (slice == null) {
            slice = NPItemHandlerSlice.of(this, start, end);
            cache.put(start, end, slice);
        }
        slice.setEnableInput(enableInput);
        return slice;
    }
}

package icu.takeneko.nekoplus.foundation.inventory;

import lombok.Setter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NPItemHandlerSlice extends ItemStackHandler {

    @Setter
    private boolean enableInput = false;

    private final int startIndex;
    private final int endIndex;
    private final NPItemHandler delegate;

    public NPItemHandlerSlice(NonNullList<ItemStack> stacks, NPItemHandler delegate, int startIndex, int endIndex) {
        super(stacks);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.delegate = delegate;
    }

    public static NPItemHandlerSlice of(NPItemHandler itemHandler, int start, int end) {
        return new NPItemHandlerSlice(new HENonNullList<>(itemHandler.getStacks().subList(start, end), ItemStack.EMPTY), itemHandler, start, end);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return delegate.isItemValid(startIndex + slot, stack);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (enableInput) {
            return super.insertItem(slot, stack, simulate);
        }
        return stack;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (enableInput) {
            super.setStackInSlot(slot, stack);
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        delegate.onContentsChanged(slot);
    }

    public static class HENonNullList<E> extends NonNullList<E> {

        public HENonNullList(List<E> list, @Nullable E defaultValue) {
            super(list, defaultValue);
        }
    }
}

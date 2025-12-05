package icu.takeneko.highenergyanvilology.foundation.inventory;

import lombok.Setter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HEItemHandlerSlice extends ItemStackHandler {

    @Setter
    private boolean enableInput = false;

    private final HEItemHandler delegate;

    public HEItemHandlerSlice(NonNullList<ItemStack> stacks, HEItemHandler delegate) {
        super(stacks);
        this.delegate = delegate;
    }

    public static HEItemHandlerSlice of(HEItemHandler itemHandler, int start, int end) {
        return new HEItemHandlerSlice(new HENonNullList<>(itemHandler.getStacks().subList(start, end), ItemStack.EMPTY), itemHandler);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return delegate.isItemValid(slot, stack);
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

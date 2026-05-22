package icu.takeneko.nekoplus.foundation.inventory;

import lombok.Setter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NPItemHandlerSlice extends ItemStacksResourceHandler {

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
        return new NPItemHandlerSlice(
            new NPNonNullList<>(itemHandler.getStacks().subList(start, end), ItemStack.EMPTY),
            itemHandler,
            start,
            end
        );
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return delegate.isValid(startIndex + index, resource);
    }

    @Override
    public int insert(ItemResource resource, int amount, TransactionContext transaction) {
        if (enableInput) {
            return super.insert(resource, amount, transaction);
        }
        return 0;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (enableInput) {
            return super.insert(index, resource, amount, transaction);
        }
        return 0;
    }

    @Override
    public void set(int index, ItemResource resource, int amount) {
        if (enableInput) {
            super.set(index, resource, amount);
        }
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        delegate.onContentsChanged(index, previousContents);
    }

    public static class NPNonNullList<E> extends NonNullList<E> {

        public NPNonNullList(List<E> list, @Nullable E defaultValue) {
            super(list, defaultValue);
        }
    }
}

package icu.takeneko.highenergyanvilology.foundation.ui.widgets;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class FilteredSlotWidget extends SlotWidget {
    private final IItemHandlerModifiable itemHandler;
    private final Container container;

    public FilteredSlotWidget() {
        this.itemHandler = null;
        this.container = null;
    }

    public FilteredSlotWidget(Container inventory, int slotIndex, int xPosition, int yPosition, boolean canTakeItems, boolean canPutItems) {
        super(inventory, slotIndex, xPosition, yPosition, canTakeItems, canPutItems);
        this.container = inventory;
        this.itemHandler = null;
    }

    public FilteredSlotWidget(IItemHandlerModifiable itemHandler, int slotIndex, int xPosition, int yPosition, boolean canTakeItems, boolean canPutItems) {
        super(itemHandler, slotIndex, xPosition, yPosition, canTakeItems, canPutItems);
        this.itemHandler = itemHandler;
        this.container = null;
    }

    public FilteredSlotWidget(IItemHandlerModifiable itemHandler, int slotIndex, int xPosition, int yPosition) {
        super(itemHandler, slotIndex, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.container = null;
    }

    public FilteredSlotWidget(Container inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
        this.container = inventory;
        this.itemHandler = null;
    }

    @Override
    public boolean canPutStack(ItemStack stack) {
        boolean superResult = super.canPutStack(stack);
        if (!superResult) return false;
        if (itemHandler != null) {
            return itemHandler.isItemValid(slotReference.index, stack);
        }
        if (container != null) {
            return container.canPlaceItem(slotReference.index, stack);
        }
        return true;
    }
}

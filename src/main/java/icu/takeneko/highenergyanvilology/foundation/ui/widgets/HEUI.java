package icu.takeneko.highenergyanvilology.foundation.ui.widgets;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandlerOwner;
import icu.takeneko.highenergyanvilology.ui.HEGuiResources;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

@SuppressWarnings("UnusedReturnValue")
public class HEUI<T extends BlockEntity & HEItemHandlerOwner> extends WidgetGroup {
    @Getter
    protected final T blockEntity;
    @Getter
    private final Component title;

    public HEUI(
        int x,
        int y,
        int width,
        int height,
        T blockEntity,
        Component title
    ) {
        super(x, y, width, height);
        this.blockEntity = blockEntity;
        this.title = title;
        label(6, 6, title);
        setBackground(HEGuiResources.UI_BACKGROUND);
    }

    protected LabelWidget label(int x, int y, Component text) {
        LabelWidget widget = new LabelWidget(x, y, text);
        widget.setDropShadow(false);
        widget.setColor(0x3E3E3E);
        addWidgets(widget);
        return widget;
    }

    protected SlotWidget filteredInputSlot(int slotIdx, int x, int y) {
        FilteredSlotWidget slot = new FilteredSlotWidget(blockEntity.getItemHandler(), slotIdx, x, y);
        slot.setBackground(HEGuiResources.ITEM_SLOT);
        addWidgets(slot);
        return slot;
    }

    protected SlotWidget slot(int slotIdx, int x, int y) {
        return slot(slotIdx, x, y, true, true);
    }

    protected SlotWidget slot(int slotIdx, int x, int y, boolean canTake, boolean canPut) {
        SlotWidget slot = new SlotWidget(blockEntity.getItemHandler(), slotIdx, x, y);
        slot.setBackground(HEGuiResources.ITEM_SLOT);
        slot.setCanPutItems(canPut).setCanTakeItems(canTake);
        addWidgets(slot);
        return slot;
    }

    protected HEPlayerInventoryWidget playerInventory(int x, int y) {
        HEPlayerInventoryWidget inventory = new HEPlayerInventoryWidget();
        inventory.setSelfPosition(x, y);
        addWidgets(inventory);
        return inventory;
    }

    protected SlotWidget outputOnlySlot(int slotIdx, int x, int y) {
        return slot(slotIdx, x, y, true, false);
    }

    protected ImageWidget image(int x, int y, int width, int height, IGuiTexture texture) {
        ImageWidget imageWidget = new ImageWidget(x, y, width, height, texture);
        addWidgets(imageWidget);
        return imageWidget;
    }
}

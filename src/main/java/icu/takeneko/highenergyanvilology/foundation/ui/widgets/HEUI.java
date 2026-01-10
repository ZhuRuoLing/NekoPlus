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

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "unused"})
public class HEUI<T extends BlockEntity & HEItemHandlerOwner> extends WidgetGroup {
    @Getter
    protected final T blockEntity;
    @Getter
    private final Component title;

    public static final int SLOT_SIZE = 18;

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

    protected SlotWidget[][] slot(int[][] slotIdx, int x, int y, int width, int height) {
        return slot(slotIdx, x, y, width, height, true, true);
    }

    protected SlotWidget[][] outputOnlySlot(int[][] slotIdx, int x, int y, int width, int height) {
        return slot(slotIdx, x, y, width, height, true, false);
    }

    protected SlotWidget[][] slot(int[][] slotIdx, int x, int y, int width, int height, boolean canTake, boolean canPut) {
        var widthPx = width * SLOT_SIZE;
        var heightPx = height * SLOT_SIZE;
        var slots = new HESlotWidget[height][width];

        for (var col = 0; col < width; col++) {
            for (var row = 0; row < height; row++) {

                var offsetX = SLOT_SIZE * col;
                var offsetY = SLOT_SIZE * row;

                var slot = new HESlotWidget(blockEntity.getItemHandler(), slotIdx[row][col], x + offsetX, y + offsetY);
                slot.setBackground(HEGuiResources.ITEM_SLOT_WEAK);

                var border = BorderPart.fromZeroIndexedPosInGrid(row, col, width, height);
                if (border != BorderPart.NONE) {
                    var texture = switch (border) {
                        case TOP, LEFT, RIGHT, BOTTOM
                            -> HEGuiResources.INVENTORY_SLOT_BORDER_TOP;
                        case TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
                            -> HEGuiResources.INVENTORY_SLOT_BORDER_TOPLEFT;
                        case COLUMN, ROW
                            ->  HEGuiResources.INVENTORY_SLOT_BORDER_COLUMN;
                        case COLUMN_TOP, COLUMN_BOTTOM, ROW_LEFT, ROW_RIGHT
                            ->  HEGuiResources.INVENTORY_SLOT_BORDER_COLUMN_TOP;
                        default
                            -> HEGuiResources.INVENTORY_SLOT_BORDER;
                    };
                    slot.setBorderTexture(
                        texture.copy().rotate(border.getRotationDegrees() / 2) // WTF??
                    );
                }

                slot.setCanPutItems(canPut).setCanTakeItems(canTake);
                addWidgets(slot);
            }
        }

//        image(x, y, widthPx, heightPx, HEGuiResources.INVENTORY_SLOT_BORDER);
        return slots;
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
        label(6,y - 5, Component.translatable("container.inventory"));
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

    public enum BorderPart {
        NONE            ,
        ALL             ,

        TOP             ,
        BOTTOM          ,
        LEFT            ,
        RIGHT           ,

        TOP_LEFT        ,
        TOP_RIGHT       ,
        BOTTOM_LEFT     ,
        BOTTOM_RIGHT    ,

        COLUMN          ,
        ROW             ,

        COLUMN_TOP      ,
        COLUMN_BOTTOM   ,
        ROW_LEFT        ,
        ROW_RIGHT       ,

        ;

        public float getRotationDegrees() {
            switch (this) {
                case BOTTOM_LEFT, LEFT, ROW_LEFT, ROW -> {
                    return -90;
                }
                case TOP_RIGHT, RIGHT, ROW_RIGHT -> {
                    return 90;
                }
                case BOTTOM_RIGHT, BOTTOM, COLUMN_BOTTOM -> {
                    return 180;
                }
                default -> {
                    return 0;
                }
            }
        }

        public static BorderPart fromZeroIndexedPosInGrid(int row, int col, int width, int height) {
            assert col >= 0 && row >= 0 && col < width && row < height;

            if (width == 1 && height == 1) {
                return ALL;
            }

            if (height == 1) {
                if (col == 0) {
                    return ROW_LEFT;
                }
                if (col == width - 1) {
                    return ROW_RIGHT;
                }
                return ROW;
            }

            if (width == 1) {
                if (row == 0) {
                    return COLUMN_TOP;
                }
                if (row == height - 1) {
                    return COLUMN_BOTTOM;
                }
                return COLUMN;
            }


            if (col == 0) {
                if (row == 0) {
                    return TOP_LEFT;
                }
                if (row == height - 1) {
                    return BOTTOM_LEFT;
                }
                return LEFT;
            }

            if (col == width - 1) {
                if (row == 0) {
                    return TOP_RIGHT;
                }
                if (row == height - 1) {
                    return BOTTOM_RIGHT;
                }
                return RIGHT;
            }

            if (row == 0) {
                return TOP;
            }
            if (row == height - 1) {
                return BOTTOM;
            }

            return NONE;
        }
    }
}

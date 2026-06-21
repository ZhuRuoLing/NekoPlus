package icu.takeneko.nekoplus.foundation.ui.widgets;

import com.lowdragmc.lowdraglib2.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.IGUIContext;
import dev.anvilcraft.lib.v2.rendering.gui.GuiRenderExtras;
import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.SlotItemHandlerWithFilter;
import icu.takeneko.nekoplus.ui.NPGuiResources;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/// A re-implementation of [dev.dubhe.anvilcraft.api.itemhandler.SlotItemHandlerWithFilter] using ldlib2
public class AncFilteredItemSlot extends ItemSlot {
    private static final ColorRectTexture FILTER_MASK = new ColorRectTexture(0x60FFAAAA);

    public AncFilteredItemSlot(Slot slot) {
        super(slot);
    }

    public AncFilteredItemSlot() {
    }

    public ItemSlot bind(FilteredItemStackHandler resourceHandler, int index) {
        bind(new SlotItemHandlerWithFilter(
            resourceHandler,
            resourceHandler::set,
            index,
            0,
            0
        ));
        return this;
    }

    @Override
    protected void drawSlotOverlay(IGUIContext context) {
        super.drawSlotOverlay(context);
        SlotItemHandlerWithFilter filterSlot = getFilteredSlot();
        if (filterSlot == null || !filterSlot.isFilter()) return;
        if (isCurrentSlotDisabled(filterSlot)) {
            if (getValue().isEmpty()) {
                drawDisabledSlot(context);
            }
            return;
        }
        FilteredItemStackHandler handler = getFilteredHandler(filterSlot);
        if (handler == null || !handler.isFilterEnabled()) return;
        if (!getValue().isEmpty()) return;

        ItemStack filterItem = filterSlot.getFilterItem(filterSlot.index);
        if (filterItem.isEmpty()) return;
        if (context instanceof GUIContext guiContext) {
            GuiRenderExtras.itemWithTransparency(guiContext.graphics, filterItem, 0, 0, 0.52F);
        }
        context.drawTexture(FILTER_MASK, 0, 0, 16, 16);
    }

    @Override
    protected void drawItemStack(IGUIContext context, ItemStack itemStack) {
        super.drawItemStack(context, itemStack);
        SlotItemHandlerWithFilter filterSlot = getFilteredSlot();
        if (filterSlot != null && isCurrentSlotDisabled(filterSlot)) {
            drawDisabledSlot(context);
        }
    }

    @Override
    protected void drawHover(IGUIContext context) {
        super.drawHover(context);
        SlotItemHandlerWithFilter filterSlot = getFilteredSlot();
        if (filterSlot != null && isCurrentSlotDisabled(filterSlot)) {
            drawDisabledSlot(context);
        }
    }

    @Override
    protected void onHoverTooltips(UIEvent event) {
        SlotItemHandlerWithFilter filterSlot = getFilteredSlot();
        if (
            filterSlot != null
                && filterSlot.isFilter()
                && isCurrentSlotDisabled(filterSlot)
        ) {
            event.hoverTooltips = HoverTooltips.create(Component.translatable("screen.anvilcraft.slot.disable.tooltip"));
            return;
        }
        super.onHoverTooltips(event);
    }

    private SlotItemHandlerWithFilter getFilteredSlot() {
        if (getSlot() instanceof SlotItemHandlerWithFilter filterSlot) {
            return filterSlot;
        }
        return null;
    }

    private FilteredItemStackHandler getFilteredHandler(SlotItemHandlerWithFilter filterSlot) {
        if (filterSlot.getResourceHandler() instanceof FilteredItemStackHandler handler) {
            return handler;
        }
        return null;
    }

    private boolean isCurrentSlotDisabled(SlotItemHandlerWithFilter filterSlot) {
        return filterSlot.isSlotDisabled(filterSlot.index);
    }

    private void drawDisabledSlot(IGUIContext context) {
        context.drawTexture(NPGuiResources.DISABLED_SLOT, 0, 0, 16, 16);
    }
}

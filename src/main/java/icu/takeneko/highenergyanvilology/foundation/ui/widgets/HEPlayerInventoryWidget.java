package icu.takeneko.highenergyanvilology.foundation.ui.widgets;

import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import icu.takeneko.highenergyanvilology.ui.HEGuiResources;

public class HEPlayerInventoryWidget extends InventorySlots {

    private IGuiTexture slotBackgroundTexture = HEGuiResources.ITEM_SLOT_WEAK;

    public HEPlayerInventoryWidget() {
        super();
        style(s -> s.backgroundTexture(HEGuiResources.INVENTORY_SLOT_BORDER5));
        for (UIElement child : getChildren()) {
            child.style(s -> s.backgroundTexture(HEGuiResources.ITEM_SLOT));
        }
    }

    @Override
    public void drawContents(boolean insideView, GUIContext guiContext) {
        super.drawContents(insideView, guiContext);
        getStyle().backgroundTexture().draw(guiContext, getPositionX(), getPositionY(), 172, 64);
        getStyle().backgroundTexture().draw(guiContext, getPositionX(), getPositionY() + 58, 172, 28);
    }
}

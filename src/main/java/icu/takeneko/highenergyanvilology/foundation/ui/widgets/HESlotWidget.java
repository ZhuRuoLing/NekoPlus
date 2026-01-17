package icu.takeneko.highenergyanvilology.foundation.ui.widgets;

import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import icu.takeneko.highenergyanvilology.ui.HEGuiResources;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.appliedenergistics.yoga.YogaEdge;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class HESlotWidget extends ItemSlot {

    protected IGuiTexture borderTexture = null;

    public HESlotWidget() {
    }

    public HESlotWidget(IItemHandlerModifiable itemHandler, int slot, int x, int y) {
        layout(l -> l.setPosition(YogaEdge.TOP, y).setPosition(YogaEdge.LEFT, x));
        style(s -> s.backgroundTexture(HEGuiResources.ITEM_SLOT));
        bind(itemHandler, slot);
    }

    public HESlotWidget setBorderTexture(IGuiTexture borderTexture) {
        this.borderTexture = borderTexture;
        return this;
    }

    @Override
    public void drawContents(boolean insideView, GUIContext guiContext) {
        super.drawContents(insideView, guiContext);
        if (borderTexture != null) {
            borderTexture.draw(guiContext, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        }
    }
}

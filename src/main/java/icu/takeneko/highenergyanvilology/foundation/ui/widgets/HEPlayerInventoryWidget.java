package icu.takeneko.highenergyanvilology.foundation.ui.widgets;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.custom.PlayerInventoryWidget;
import icu.takeneko.highenergyanvilology.ui.HEGuiResources;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class HEPlayerInventoryWidget extends PlayerInventoryWidget {

    private IGuiTexture slotBackgroundTexture = HEGuiResources.ITEM_SLOT_WEAK;

    public HEPlayerInventoryWidget() {
        super();
        super.setSlotBackground(IGuiTexture.EMPTY);
        this.setBackground(HEGuiResources.INVENTORY_SLOT_BORDER5);
    }

    @Override
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (Widget widget : widgets) {
            if (widget instanceof SlotWidget slotWidget) {
                slotBackgroundTexture.draw(
                    graphics,
                    mouseX,
                    mouseY,
                    slotWidget.getPositionX(),
                    slotWidget.getPositionY(),
                    slotWidget.getSizeWidth(),
                    slotWidget.getSizeHeight()
                );
            }
        }
        backgroundTexture.draw(graphics, mouseX, mouseY, this.getPositionX(), this.getPositionY(), 172, 64);
        backgroundTexture.draw(graphics, mouseX, mouseY, this.getPositionX(), this.getPositionY() + 58, 172, 28);
        drawWidgetsBackground(graphics, mouseX, mouseY, partialTicks);
    }

    public void setSlotBackground(IGuiTexture slotBackground) {
        this.slotBackgroundTexture = slotBackground;
    }
}

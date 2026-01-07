package icu.takeneko.highenergyanvilology.foundation.ui.widgets;

import com.lowdragmc.lowdraglib.gui.widget.custom.PlayerInventoryWidget;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class HEPlayerInventoryWidget extends PlayerInventoryWidget {

    public HEPlayerInventoryWidget() {
        super();
    }

    @Override
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawWidgetsBackground(graphics, mouseX, mouseY, partialTicks);
        drawBackgroundTexture(graphics, mouseX, mouseY);
    }
}

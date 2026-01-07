package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import icu.takeneko.highenergyanvilology.HEAnvilology;

public class HEGuiResources {
    public static final ResourceBorderTexture UI_BACKGROUND = new ResourceBorderTexture(HEAnvilology.location("textures/gui/background.png").toString(), 16, 16, 4, 4);
    public final static ResourceBorderTexture ITEM_SLOT = new ResourceBorderTexture(HEAnvilology.location("textures/gui/slot.png").toString(), 18, 18, 1, 1);
    public final static ResourceBorderTexture INVENTORY_SLOT_BORDER5 = new ResourceBorderTexture(HEAnvilology.location("textures/gui/slot_inventory_border5.png").toString(), 28, 28, 6, 6);
    public final static ResourceBorderTexture ITEM_SLOT_WEAK = new ResourceBorderTexture(HEAnvilology.location("textures/gui/slot_weak.png").toString(), 18, 18, 1, 1);

    public static ProgressTexture getProgressTexture() {
        ProgressTexture progressTexture = new ProgressTexture(
            new ResourceTexture(HEAnvilology.location("textures/gui/progress_bar_arrow.png")).getSubTexture(0, 0, 1, 0.5),
            new ResourceTexture(HEAnvilology.location("textures/gui/progress_bar_arrow.png")).getSubTexture(0, 0.5, 1, 0.5)
        );
        progressTexture.setProgress(0);
        return progressTexture;
    }

}

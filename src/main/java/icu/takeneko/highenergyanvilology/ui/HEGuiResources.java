package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib2.gui.texture.AnimationTexture;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import icu.takeneko.highenergyanvilology.HEAnvilology;

public class HEGuiResources {
    public static final SpriteTexture UI_BACKGROUND = SpriteTexture.of(HEAnvilology.location("textures/gui/background.png")).setBorder(4, 4, 16, 16);
    public final static SpriteTexture ITEM_SLOT = SpriteTexture.of(HEAnvilology.location("textures/gui/slot.png")).setBorder(1, 1, 18, 18);
    public final static SpriteTexture INVENTORY_SLOT_BORDER = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border.png")).setBorder(1, 1, 18, 18);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_TOP = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_top.png")).setBorder(1, 1, 18, 18);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_TOPLEFT = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_topleft.png")).setBorder(1, 1, 18, 18);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_COLUMN = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_column.png")).setBorder(1, 1, 18, 18);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_COLUMN_TOP = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_column_top.png")).setBorder(1, 1, 18, 18);
    public final static SpriteTexture INVENTORY_SLOT_BORDER5 = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border5.png")).setBorder(6, 6, 28, 28);
    public final static SpriteTexture ITEM_SLOT_WEAK = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_weak.png")).setBorder(1, 1, 18, 18);

//    public static ProgressTexture getProgressTexture() {
//        AnimationTexture texture = new AnimationTexture(HEAnvilology.location("textures/gui/progress_bar_arrow.png"));
//
//        ProgressTexture progressTexture = new ProgressTexture(
//            new ResourceTexture(HEAnvilology.location("textures/gui/progress_bar_arrow.png")).getSubTexture(0, 0, 1, 0.5),
//            new ResourceTexture(HEAnvilology.location("textures/gui/progress_bar_arrow.png")).getSubTexture(0, 0.5, 1, 0.5)
//        );
//        progressTexture.setProgress(0);
//        return progressTexture;
//    }

}

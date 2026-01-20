package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib2.editor.resource.BuiltinResourceProvider;
import com.lowdragmc.lowdraglib2.editor.resource.ResourceInstance;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import com.lowdragmc.lowdraglib2.gui.ui.style.Stylesheet;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import com.lowdragmc.lowdraglib2.math.Size;
import icu.takeneko.highenergyanvilology.HEAnvilology;

public class HEGuiResources {
    public static final SpriteTexture UI_BACKGROUND = SpriteTexture.of(HEAnvilology.location("textures/gui/background.png"))
        .setSpriteSize(Size.of(16, 16))
        .setBorder(4, 4, 4, 4);
    public final static SpriteTexture ITEM_SLOT = SpriteTexture.of(HEAnvilology.location("textures/gui/slot.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);
    public final static SpriteTexture ITEM_SLOT_WEAK = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_weak.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);
    public final static SpriteTexture ITEM_SLOT_UNBORDERED = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_unbordered.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);
    public final static SpriteTexture INVENTORY_SLOT_BORDER = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_TOP = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_top.png")).setBorder(1, 1, 1, 1);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_TOPLEFT = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_topleft.png")).setBorder(1, 1, 1, 1);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_COLUMN = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_column.png")).setBorder(1, 1, 1, 1);
    public final static SpriteTexture INVENTORY_SLOT_BORDER_COLUMN_TOP = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border_column_top.png")).setBorder(1, 1, 1, 1);
    public final static SpriteTexture INVENTORY_SLOT_BORDER5 = SpriteTexture.of(HEAnvilology.location("textures/gui/slot_inventory_border5.png")).setBorder(6, 6, 28, 28);

    public static final Stylesheet STYLESHEET = StylesheetManager.INSTANCE.getStylesheetSafe(HEAnvilology.location(StylesheetManager.PATH + "/he.lss"));

    public static void setupRegistration(ResourceInstance<IGuiTexture> resourceInstance) {
        BuiltinResourceProvider<IGuiTexture> provider = new BuiltinResourceProvider<>("heui", resourceInstance);
        provider.addResource("ui_background", UI_BACKGROUND);
        provider.addResource("slot", ITEM_SLOT);
        provider.addResource("slot_border", INVENTORY_SLOT_BORDER);
        provider.addResource("slot_weak", ITEM_SLOT_WEAK);
        provider.addResource("slot_unbordered", ITEM_SLOT_UNBORDERED);
        resourceInstance.addBuiltinProvider(provider);
    }


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

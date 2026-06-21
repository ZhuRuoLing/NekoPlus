package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.editor.resource.BuiltinResourceProvider;
import com.lowdragmc.lowdraglib2.editor.resource.ResourceInstance;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.texture.Icons;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Scroller;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextArea;
import com.lowdragmc.lowdraglib2.gui.ui.style.Stylesheet;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.Sprites;
import com.lowdragmc.lowdraglib2.math.Size;
import icu.takeneko.nekoplus.NekoPlus;

public class NPGuiResources {
    public static final SpriteTexture UI_BACKGROUND = SpriteTexture.of(NekoPlus.location("textures/gui/background.png"))
        .setSpriteSize(Size.of(16, 16))
        .setBorder(4, 4, 4, 4);
    public static final SpriteTexture TEXT_AREA_BACKGROUND = SpriteTexture.of(NekoPlus.location(
            "textures/gui/text_area_background.png"))
        .setSpriteSize(Size.of(16, 16))
        .setBorder(4, 4, 4, 4);
    public static final SpriteTexture BUTTON_HOVERED = SpriteTexture.of(NekoPlus.location(
            "textures/gui/button_hovered.png"))
        .setSpriteSize(Size.of(16, 16))
        .setBorder(4, 4, 4, 4);
    public static final SpriteTexture BUTTON_PRESSED = SpriteTexture.of(NekoPlus.location(
            "textures/gui/button_pressed.png"))
        .setSpriteSize(Size.of(16, 16))
        .setBorder(4, 4, 4, 4);

    public final static SpriteTexture ITEM_SLOT = SpriteTexture.of(NekoPlus.location("textures/gui/slot.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);
    public final static SpriteTexture ITEM_SLOT_WEAK = SpriteTexture.of(NekoPlus.location("textures/gui/slot_weak.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);
    public final static SpriteTexture ITEM_SLOT_UNBORDERED = SpriteTexture.of(NekoPlus.location(
            "textures/gui/slot_unbordered.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);
    public final static SpriteTexture INVENTORY_SLOT_BORDER = SpriteTexture.of(NekoPlus.location(
            "textures/gui/slot_inventory_border.png"))
        .setSpriteSize(Size.of(18, 18))
        .setBorder(1, 1, 1, 1);

    public static final SpriteTexture PROGRESS_ARROW_BG = SpriteTexture.of(NekoPlus.location(
            "textures/gui/progress_bar_arrow.png"))
        .setSprite(0, 0, 20, 20);
    public static final SpriteTexture PROGRESS_ARROW_FG = SpriteTexture.of(NekoPlus.location(
            "textures/gui/progress_bar_arrow.png"))
        .setSprite(0, 20, 20, 20)
        .setWrapMode(SpriteTexture.WrapMode.REPEAT);

    public static final SpriteTexture CROSS = SpriteTexture.of(NekoPlus.location("textures/gui/cross_small.png"))
        .setSprite(0, 0, 7, 7);

    public static final SpriteTexture DOWN_ARROW = SpriteTexture.of(NekoPlus.location("textures/gui/down_arrow.png"))
        .setSprite(0, 0, 14, 14);

    public static final SpriteTexture SCROLLER_UP_ARROW = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(0, 0, 5, 4);

    public static final SpriteTexture SCROLLER_DOWN_ARROW = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(5, 0, 5, 4);

    public static final SpriteTexture SCROLLER_LEFT_ARROW = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(10, 0, 4, 5);

    public static final SpriteTexture SCROLLER_RIGHT_ARROW = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(15, 0, 4, 5);

    public static final SpriteTexture SCROLLER_BAR_V = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(0, 12, 5, 7)
        .setBorder(2, 2, 2, 2);

    public static final SpriteTexture SCROLLER_BUTTON_V = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(0, 5, 5, 7)
        .setBorder(2, 2, 2, 2);

    public static final SpriteTexture SCROLLER_BUTTON_V_HOVERED = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(12, 5, 5, 7)
        .setBorder(2, 2, 2, 2);

    public static final SpriteTexture SCROLLER_BAR_H = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(5, 12, 7, 5)
        .setBorder(2, 2, 2, 2);

    public static final SpriteTexture SCROLLER_BUTTON_H = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(5, 5, 7, 5)
        .setBorder(2, 2, 2, 2);

    public static final SpriteTexture SCROLLER_BUTTON_H_HOVERED = SpriteTexture.of(NekoPlus.location(
            "textures/gui/scroller_sprites.png"))
        .setSprite(17, 5, 7, 5)
        .setBorder(2, 2, 2, 2);


    public static final Stylesheet STYLESHEET = StylesheetManager.INSTANCE.getStylesheetSafe(NekoPlus.location(
        StylesheetManager.PATH + "/he.lss"));

    public static void setupRegistration(ResourceInstance<IGuiTexture> resourceInstance) {
        BuiltinResourceProvider<IGuiTexture> provider = new BuiltinResourceProvider<>("heui", resourceInstance);
        provider.addResource("ui_background", UI_BACKGROUND);
        provider.addResource("slot", ITEM_SLOT);
        provider.addResource("slot_border", INVENTORY_SLOT_BORDER);
        provider.addResource("slot_weak", ITEM_SLOT_WEAK);
        provider.addResource("slot_unbordered", ITEM_SLOT_UNBORDERED);
        resourceInstance.addBuiltinProvider(provider);
    }

    public static void setupScrollerTexture(TextArea textArea) {
        setupScrollerTexture(textArea.horizontalScroller);
        setupScrollerTexture(textArea.verticalScroller);
    }

    public static void setupScrollerTexture(Scroller scroller) {
        if (scroller instanceof Scroller.Horizontal horizontal) {
            setupScrollerTexture(horizontal);
        }
        if (scroller instanceof Scroller.Vertical vertical) {
            setupScrollerTexture(vertical);
        }
    }

    public static void setupScrollerTexture(Scroller.Horizontal scroller) {
        scroller.headButton.buttonStyle(s -> s
            .baseTexture(SCROLLER_LEFT_ARROW)
            .hoverTexture(SCROLLER_LEFT_ARROW)
            .pressedTexture(SCROLLER_LEFT_ARROW)
        );

        scroller.tailButton.buttonStyle(style -> style
            .baseTexture(SCROLLER_RIGHT_ARROW)
            .hoverTexture(SCROLLER_RIGHT_ARROW)
            .pressedTexture(SCROLLER_RIGHT_ARROW)
        );
        scroller.scrollContainer.style(style -> style
            .backgroundTexture(SCROLLER_BAR_H)
        );
        scroller.scrollBar.buttonStyle(style -> style
            .baseTexture(SCROLLER_BUTTON_H)
            .hoverTexture(SCROLLER_BUTTON_H)
            .pressedTexture(SCROLLER_BUTTON_H_HOVERED)
        );
    }

    public static void setupScrollerTexture(Scroller.Vertical scroller) {
        scroller.headButton.buttonStyle(s -> s
            .baseTexture(SCROLLER_UP_ARROW)
            .hoverTexture(SCROLLER_UP_ARROW)
            .pressedTexture(SCROLLER_UP_ARROW)
        );

        scroller.tailButton.buttonStyle(style -> style
            .baseTexture(SCROLLER_DOWN_ARROW)
            .hoverTexture(SCROLLER_DOWN_ARROW)
            .pressedTexture(SCROLLER_DOWN_ARROW)
        );
        scroller.scrollContainer.style(style -> style
            .backgroundTexture(SCROLLER_BAR_V)
        );
        scroller.scrollBar.buttonStyle(style -> style
            .baseTexture(SCROLLER_BUTTON_V)
            .hoverTexture(SCROLLER_BUTTON_V)
            .pressedTexture(SCROLLER_BUTTON_V_HOVERED)
        );
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

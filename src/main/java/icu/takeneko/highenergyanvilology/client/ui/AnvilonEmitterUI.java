package icu.takeneko.highenergyanvilology.client.ui;

import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.iventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.Sprites;
import net.minecraft.network.chat.Component;
import org.appliedenergistics.yoga.YogaAlign;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.appliedenergistics.yoga.YogaJustify;
import org.appliedenergistics.yoga.style.StyleSizeLength;

public class AnvilonEmitterUI extends UIElement {
    private final ItemSlot slot = new ItemSlot();

    public AnvilonEmitterUI() {
        layout(props -> {
            props.setWidthPercent(100);
            props.setHeightPercent(100);
            props.setWidth(StyleSizeLength.STRETCH);
            props.setHeight(StyleSizeLength.STRETCH);
            props.setJustifyContent(YogaJustify.CENTER);
            props.setAlignItems(YogaAlign.CENTER);
        });
        style(style -> {
            style.backgroundTexture(Sprites.BORDER_RT0);
        });
        UIElement container = new UIElement();
        TextElement textElement = new TextElement();
        textElement.setText("TITLE");
        textElement.getLayout().setWidthPercent(100);
        container.addChildren(
            textElement,
            slot.layout(layout -> {
                layout.setAlignSelf(YogaAlign.CENTER);
            }),
            new InventorySlots().layout(layout -> {
                layout.setPadding(YogaEdge.TOP, 8);
            })
        );
        container.getLayout().setFlexDirection(YogaFlexDirection.COLUMN);
        container.getLayout().setPadding(YogaEdge.ALL, 6);
        addChild(container);
    }
}

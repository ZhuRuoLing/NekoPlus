package icu.takeneko.highenergyanvilology.client.ui;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.iventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.Sprites;
import org.appliedenergistics.yoga.YogaAlign;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaFlexDirection;

public class AnvilonEmitterUI extends UIElement {

    public AnvilonEmitterUI() {
        addChildren(
            new TextElement()
                .setText("TITLE")
                .textStyle(textStyle ->
                    textStyle.textAlignHorizontal(Horizontal.CENTER)
                ).layout(layout -> {
                    layout.setHeight(12);
                    layout.setWidthPercent(100);
                }),
            new ItemSlot()
                .layout(layout -> {
                    layout.setAlignSelf(YogaAlign.CENTER);
                }),
            new InventorySlots()
                .layout(layout -> {
                    layout.setPadding(YogaEdge.TOP, 8);
                    layout.setAlignSelf(YogaAlign.CENTER);
                })
        ).layout(layout -> {
            layout.setWidth(172);
            layout.setHeight(126);
            layout.setFlexDirection(YogaFlexDirection.COLUMN);
            layout.setPadding(YogaEdge.ALL, 6);
        }).style(style -> {
            style.backgroundTexture(Sprites.BORDER_RT0);
        });
    }
}

package icu.takeneko.nekoplus.foundation.ui;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import dev.vfyjxf.taffy.style.FlexDirection;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerOwner;
import icu.takeneko.nekoplus.ui.NPGuiResources;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "unused"})
public class NPUI<T extends BlockEntity> extends UIElement {
    @Getter
    protected final T blockEntity;
    @Getter
    private final Component title;

    public static final int SLOT_SIZE = 18;

    public NPUI(
        T blockEntity,
        Component title
    ) {
        super();
        this.blockEntity = blockEntity;
        this.title = title;
        addClass("he-ui-container");
        addChild(
            new TextElement()
                .setText(title)
                .textStyle(ts -> ts.adaptiveHeight(true))
        );
    }

    public static <T extends BlockEntity> ModularUI of(NPUI<T> ui, BlockUIMenuType.BlockUIHolder holder) {
        return new ModularUI(UI.of(ui, NPGuiResources.STYLESHEET), holder.player);
    }

    public static UIElement layout(
        UIElement... children
    ) {
        var e = new UIElement();
        e.addChildren(children);
        return e;
    }
    public static UIElement horizontalLayout(
        UIElement... children
    ) {
        return layout(children).layout(l -> l.flexDirection(FlexDirection.ROW));
    }
}

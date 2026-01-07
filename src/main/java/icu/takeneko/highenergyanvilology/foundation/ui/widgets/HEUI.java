package icu.takeneko.highenergyanvilology.foundation.ui.widgets;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import icu.takeneko.highenergyanvilology.ui.HEGuiResources;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HEUI<T extends BlockEntity> extends WidgetGroup {
    protected final T blockEntity;
    private final Component title;

    public HEUI(
        int x,
        int y,
        int width,
        int height,
        T blockEntity,
        Component title
    ) {
        super(x, y, width, height);
        this.blockEntity = blockEntity;
        this.title = title;
        label(6, 6, title);
        setBackground(HEGuiResources.UI_BACKGROUND);
    }

    protected LabelWidget label(int x, int y, Component text) {
        LabelWidget widget = new LabelWidget(x, y, text);
        widget.setDropShadow(false);
        widget.setColor(0x3E3E3E);
        addWidgets(widget);
        return widget;
    }


}

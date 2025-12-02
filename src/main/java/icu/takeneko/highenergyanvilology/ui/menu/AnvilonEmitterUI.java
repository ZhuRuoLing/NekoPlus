package icu.takeneko.highenergyanvilology.ui.menu;

import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.custom.PlayerInventoryWidget;
import com.lowdragmc.lowdraglib.gui.widget.layout.Align;
import icu.takeneko.highenergyanvilology.block.entity.AnvilonEmitterBlockEntity;
import net.minecraft.network.chat.Component;

public class AnvilonEmitterUI extends WidgetGroup {
    private final AnvilonEmitterBlockEntity blockEntity;

    public AnvilonEmitterUI(AnvilonEmitterBlockEntity blockEntity) {
        super(0, 0, 172, 126);
        this.blockEntity = blockEntity;
        PlayerInventoryWidget inventory = new PlayerInventoryWidget();
        inventory.setSelfPosition(0, 40);
        SlotWidget slotWidget = new SlotWidget(blockEntity.getItemHandler(), 0, 77, 20);
        LabelWidget textWidget = new LabelWidget(0, 0, Component.translatable("block.highenergyanvilology.anvilon_emitter"));
        textWidget.setSize(172, 10);
        textWidget.setAlign(Align.TOP_CENTER);
        WidgetGroup aligner = new WidgetGroup(0, 7, 172, 10);
        aligner.addWidgets(textWidget);
        addWidgets(slotWidget, aligner, inventory);
        setBackground(ResourceBorderTexture.BORDERED_BACKGROUND);
    }


}

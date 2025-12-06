package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.custom.PlayerInventoryWidget;
import com.lowdragmc.lowdraglib.gui.widget.layout.Align;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import net.minecraft.network.chat.Component;

public class ParticleStabilizerUI extends WidgetGroup {
    private final ParticleStabilizerBlockEntity blockEntity;


    public ParticleStabilizerUI(ParticleStabilizerBlockEntity blockEntity) {
        super(0, 0, 172, 126);
        this.blockEntity = blockEntity;
        PlayerInventoryWidget inventory = new PlayerInventoryWidget();
        inventory.setSelfPosition(0, 40);
        SlotWidget inputSlot = new SlotWidget(blockEntity.getItemHandler(), 0, 59, 20);
        SlotWidget outputSlot = new SlotWidget(blockEntity.getItemHandler(), 1, 95, 20);
        outputSlot.setCanPutItems(false);
        LabelWidget textWidget = new LabelWidget(0, 0, Component.translatable("block.highenergyanvilology.particle_stabilizer"));
        textWidget.setSize(172, 10);
        textWidget.setAlign(Align.TOP_CENTER);
        WidgetGroup aligner = new WidgetGroup(0, 7, 172, 10);
        aligner.addWidgets(textWidget);
        addWidgets(aligner, inputSlot, outputSlot, inventory);
        setBackground(ResourceBorderTexture.BORDERED_BACKGROUND);
    }
}

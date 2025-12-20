package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.custom.PlayerInventoryWidget;
import com.lowdragmc.lowdraglib.gui.widget.layout.Align;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.ui.widgets.FilteredSlotWidget;
import icu.takeneko.highenergyanvilology.recipes.AirCondensingRecipe;
import net.minecraft.network.chat.Component;

public class ParticleStabilizerUI extends WidgetGroup implements Tickable {
    private final ParticleStabilizerBlockEntity blockEntity;
    private final ProgressTexture progressTexture = new ProgressTexture(
        new ResourceTexture("ldlib:textures/gui/progress_bar_arrow.png").getSubTexture(0, 0, 1, 0.5),
        new ResourceTexture("ldlib:textures/gui/progress_bar_arrow.png").getSubTexture(0, 0.5, 1, 0.5)
    );

    public ParticleStabilizerUI(ParticleStabilizerBlockEntity blockEntity) {
        super(0, 0, 172, 146);
        progressTexture.setFillDirection(ProgressTexture.FillDirection.LEFT_TO_RIGHT);
        progressTexture.setProgress(0.5);
        this.blockEntity = blockEntity;
        PlayerInventoryWidget inventory = new PlayerInventoryWidget();
        inventory.setSelfPosition(0, 58);
        ImageWidget arrow = new ImageWidget(68, 28, 20, 20, () -> progressTexture);
        FilteredSlotWidget inputSlot = new FilteredSlotWidget(blockEntity.getItemHandler(), 0, 41, 29);
        SlotWidget outputSlot1 = new SlotWidget(blockEntity.getItemHandler(), 1, 95, 20);
        SlotWidget outputSlot2 = new SlotWidget(blockEntity.getItemHandler(), 2, 113, 20);
        SlotWidget outputSlot3 = new SlotWidget(blockEntity.getItemHandler(), 3, 95, 38);
        SlotWidget outputSlot4 = new SlotWidget(blockEntity.getItemHandler(), 4, 113, 38);
        outputSlot1.setCanPutItems(false);
        outputSlot2.setCanPutItems(false);
        outputSlot3.setCanPutItems(false);
        outputSlot4.setCanPutItems(false);
        LabelWidget textWidget = new LabelWidget(0, 0, Component.translatable("block.highenergyanvilology.particle_stabilizer"));
        textWidget.setSize(172, 10);
        textWidget.setAlign(Align.TOP_CENTER);
        WidgetGroup aligner = new WidgetGroup(0, 7, 172, 10);
        aligner.addWidgets(textWidget);
        addWidgets(aligner, inputSlot, arrow, outputSlot1, outputSlot2, outputSlot3, outputSlot4, inventory);
        setBackground(ResourceBorderTexture.BORDERED_BACKGROUND);
    }

    @Override
    public void tick() {
        int maxProgress = blockEntity.getMaxProgress();
        if (maxProgress > 0) {
            float progress = (float) blockEntity.getProgress() / maxProgress;
            progressTexture.setProgress(progress);
            return;
        }
        progressTexture.setProgress(0);
    }
}

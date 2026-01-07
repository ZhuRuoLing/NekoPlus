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
import icu.takeneko.highenergyanvilology.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.ui.widgets.FilteredSlotWidget;
import icu.takeneko.highenergyanvilology.foundation.ui.widgets.HEPlayerInventoryWidget;
import icu.takeneko.highenergyanvilology.foundation.ui.widgets.HEUI;
import net.minecraft.network.chat.Component;

public class ParticleStabilizerUI extends HEUI<ParticleStabilizerBlockEntity> implements Tickable {
    private final ProgressTexture progressTexture = HEGuiResources.getProgressTexture();

    public ParticleStabilizerUI(ParticleStabilizerBlockEntity blockEntity) {
        super(0, 0, 172, 156, blockEntity, Component.translatable("block.highenergyanvilology.particle_stabilizer"));
        progressTexture.setFillDirection(ProgressTexture.FillDirection.LEFT_TO_RIGHT);
        progressTexture.setProgress(0);
        HEPlayerInventoryWidget inventory = new HEPlayerInventoryWidget();
        inventory.setSlotBackground(HEGuiResources.ITEM_SLOT_WEAK);
        inventory.setBackground(HEGuiResources.INVENTORY_SLOT_BORDER5);
        inventory.setSelfPosition(0, 68);
        ImageWidget arrow = new ImageWidget(68, 28, 20, 20, progressTexture);
        FilteredSlotWidget inputSlot = new FilteredSlotWidget(blockEntity.getItemHandler(), 0, 41, 29);
        inputSlot.setBackground(HEGuiResources.ITEM_SLOT);
        SlotWidget outputSlot1 = new SlotWidget(blockEntity.getItemHandler(), 1, 95, 19);
        SlotWidget outputSlot2 = new SlotWidget(blockEntity.getItemHandler(), 2, 115, 19);
        SlotWidget outputSlot3 = new SlotWidget(blockEntity.getItemHandler(), 3, 95, 39);
        SlotWidget outputSlot4 = new SlotWidget(blockEntity.getItemHandler(), 4, 115, 39);
        outputSlot1.setCanPutItems(false).setBackground(HEGuiResources.ITEM_SLOT);
        outputSlot2.setCanPutItems(false).setBackground(HEGuiResources.ITEM_SLOT);
        outputSlot3.setCanPutItems(false).setBackground(HEGuiResources.ITEM_SLOT);
        outputSlot4.setCanPutItems(false).setBackground(HEGuiResources.ITEM_SLOT);
        label(6,62, Component.translatable("container.inventory"));
        addWidgets(inputSlot, arrow, outputSlot1, outputSlot2, outputSlot3, outputSlot4, inventory);
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

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
        image(68, 28, 20, 20, progressTexture.setFillDirection(ProgressTexture.FillDirection.LEFT_TO_RIGHT));
        playerInventory(0, 68);
        filteredInputSlot(0, 41, 29);
        outputOnlySlot(1, 95, 19);
        outputOnlySlot(2, 115, 19);
        outputOnlySlot(3, 95, 39);
        outputOnlySlot(4, 115, 39);
        label(6, 62, Component.translatable("container.inventory"));
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

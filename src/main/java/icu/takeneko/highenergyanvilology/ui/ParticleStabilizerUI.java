package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import icu.takeneko.highenergyanvilology.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.ui.widgets.HEUI;
import net.minecraft.network.chat.Component;

public class ParticleStabilizerUI extends HEUI<ParticleStabilizerBlockEntity> implements Tickable {
    private final ProgressTexture progressTexture = HEGuiResources.getProgressTexture();

    public ParticleStabilizerUI(ParticleStabilizerBlockEntity blockEntity) {
        super(0, 0, 172, 156, blockEntity, Component.translatable("block.highenergyanvilology.particle_stabilizer"));

        image(68, 28, 20, 20, progressTexture.setFillDirection(ProgressTexture.FillDirection.LEFT_TO_RIGHT));

        filteredInputSlot(0, 41, 29);

        int[][] outputSlotIdxes = {{1, 2}, {3, 4}};
        outputOnlySlot(outputSlotIdxes, 95, 19, 2, 2);

        label(6, 62, Component.translatable("container.inventory"));
        playerInventory(0, 68);
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

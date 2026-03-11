package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import net.minecraft.network.chat.Component;
import org.appliedenergistics.yoga.YogaAlign;
import org.appliedenergistics.yoga.YogaJustify;

public class ParticleStabilizerUI extends NPUI<ParticleStabilizerBlockEntity> implements Tickable {
    //private final ProgressTexture progressTexture = HEGuiResources.getProgressTexture();

    public ParticleStabilizerUI(ParticleStabilizerBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.particle_stabilizer"));

        addChildren(
            horizontalLayout(
                new ItemSlot()
                    .bind(blockEntity.getItemHandler(), 0)
                    .addClass("bordered_slot"),
                layout(
                    horizontalLayout(
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 1),
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 2)
                    ),
                    horizontalLayout(
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 3),
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 4)
                    )
                ).layout(l ->
                    l.minWidth(0)
                ).addClass("bordered")
            ).layout(l ->
                l.alignItems(YogaAlign.CENTER)
                    .setJustifyContent(YogaJustify.SPACE_EVENLY)
                    .widthPercent(100)
            ),
            new TextElement()
                .setText(Component.translatable("container.inventory"))
                .textStyle(ts -> ts.adaptiveHeight(true)),
            new InventorySlots()
        );
    }

    @Override
    public void tick() {
//        int maxProgress = blockEntity.getMaxProgress();
//        if (maxProgress > 0) {
//            float progress = (float) blockEntity.getProgress() / maxProgress;
//            progressTexture.setProgress(progress);
//            return;
//        }
//        progressTexture.setProgress(0);
    }
}

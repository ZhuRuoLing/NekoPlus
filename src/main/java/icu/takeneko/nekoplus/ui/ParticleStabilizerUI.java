package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import net.minecraft.network.chat.Component;

public class ParticleStabilizerUI extends NPUI<ParticleStabilizerBlockEntity> implements Tickable {

    public ParticleStabilizerUI(ParticleStabilizerBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.particle_stabilizer"));
        addTitle();
        ProgressBar progressBar = new ProgressBar()
            .setValue(0.5f)
            .label(l -> l.setText(""))
            .bar(it ->
                it.style(it1 -> it1.backgroundTexture(NPGuiResources.PROGRESS_ARROW_FG))
                    .layout(lyt -> lyt.paddingAll(0))
            )
            .barContainer(it ->
                it.style(it1 -> it1.backgroundTexture(NPGuiResources.PROGRESS_ARROW_BG))
                    .layout(lyt -> lyt.paddingAll(0))
            );
        progressBar.bind(DataBindingBuilder.floatValS2C(() -> blockEntity.getProgress() / ((float) blockEntity.getMaxProgress())).build());
        progressBar.layout(it -> it.minWidth(20).minHeight(20));
        progressBar.progressBarStyle(it -> it.interpolate(false));
        addChildren(
            row(
                new ItemSlot()
                    .bind(blockEntity.getItemHandler(), 0)
                    .addClass("bordered_slot"),
                progressBar,
                div(
                    row(
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 1),
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 2)
                    ),
                    row(
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 3),
                        new ItemSlot()
                            .bind(blockEntity.getItemHandler(), 4)
                    )
                ).layout(l ->
                    l.minWidth(0)
                ).addClass("bordered")
            ).layout(l ->
                l.alignItems(AlignItems.CENTER)
                    .justifyContent(AlignContent.SPACE_EVENLY)
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
    }
}

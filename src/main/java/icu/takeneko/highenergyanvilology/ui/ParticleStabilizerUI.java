package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import icu.takeneko.highenergyanvilology.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.ui.HEUI;
import net.minecraft.network.chat.Component;
import org.appliedenergistics.yoga.YogaAlign;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.appliedenergistics.yoga.YogaJustify;

public class ParticleStabilizerUI extends HEUI<ParticleStabilizerBlockEntity> implements Tickable {
    public static final int[][] OUTPUT_SLOT_INDEXES = {{1, 2}, {3, 4}};
    //private final ProgressTexture progressTexture = HEGuiResources.getProgressTexture();

    public ParticleStabilizerUI(ParticleStabilizerBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.highenergyanvilology.particle_stabilizer"));
        UIElement slots = new UIElement();
        slots.layout(l ->
            l.flexDirection(YogaFlexDirection.ROW)
                .alignItems(YogaAlign.CENTER)
                .setJustifyContent(YogaJustify.SPACE_EVENLY)
                .widthPercent(100)
        );
        UIElement outputs = new UIElement();
        UIElement row1 = new UIElement();
        row1.layout(l -> l.flexDirection(YogaFlexDirection.ROW));
        row1.addChildren(
            new ItemSlot()
                .bind(blockEntity.getItemHandler(), 1)
                .addClass("slot_unbordered"),
            new ItemSlot()
                .bind(blockEntity.getItemHandler(), 2)
                .addClass("slot_unbordered")
        );
        UIElement row2 = new UIElement();
        row2.layout(l -> l.flexDirection(YogaFlexDirection.ROW));
        row2.addChildren(
            new ItemSlot()
                .bind(blockEntity.getItemHandler(), 3)
                .addClass("slot_unbordered"),
            new ItemSlot()
                .bind(blockEntity.getItemHandler(), 4)
                .addClass("slot_unbordered")
        );
        outputs.addChildren(row1, row2);
        outputs.addClass("bordered");
        outputs.layout(l -> l.minWidth(0));
        slots.addChildren(
            new ItemSlot()
                .bind(blockEntity.getItemHandler(), 0)
                .addClass("bordered_slot"),
            outputs
        );
        addChild(slots);
        addChildren(
            new TextElement().setText(Component.translatable("container.inventory")).textStyle(ts -> ts.adaptiveHeight(true)),
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

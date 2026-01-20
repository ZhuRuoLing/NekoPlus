package icu.takeneko.highenergyanvilology.ui;

import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.ui.HEUI;
import net.minecraft.network.chat.Component;

public class AnvilonEmitterUI extends HEUI<AnvilonEmitterBlockEntity> {

    public AnvilonEmitterUI(AnvilonEmitterBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.highenergyanvilology.anvilon_emitter"));
        addChildren(new InventorySlots());
    }
}

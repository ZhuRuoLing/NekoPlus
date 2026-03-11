package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import icu.takeneko.nekoplus.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import net.minecraft.network.chat.Component;

public class AnvilonEmitterUI extends NPUI<AnvilonEmitterBlockEntity> {

    public AnvilonEmitterUI(AnvilonEmitterBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.anvilon_emitter"));
        addChildren(new InventorySlots());
    }
}

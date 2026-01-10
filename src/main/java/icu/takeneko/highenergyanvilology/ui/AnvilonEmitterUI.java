package icu.takeneko.highenergyanvilology.ui;

import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.ui.widgets.HEUI;
import net.minecraft.network.chat.Component;

public class AnvilonEmitterUI extends HEUI<AnvilonEmitterBlockEntity> {

    public AnvilonEmitterUI(AnvilonEmitterBlockEntity blockEntity) {
        super(0, 0, 172, 136, blockEntity, Component.translatable("block.highenergyanvilology.anvilon_emitter"));
        playerInventory(0, 50);
        slot(0, 77, 20);

    }
}

package icu.takeneko.nekoplus.foundation.ui;

import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerOwner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NPInventoryUI<T extends BlockEntity & NPItemHandlerOwner> extends NPUI<T>{
    public NPInventoryUI(T blockEntity, Component title) {
        super(blockEntity, title);
    }
}

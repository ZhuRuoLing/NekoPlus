package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import net.minecraft.network.chat.Component;

public class ProgrammableLogicGateUI extends NPUI<ProgrammableLogicGateBlockEntity> {
    public ProgrammableLogicGateUI(ProgrammableLogicGateBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.programmable_logic_gate"));
        addChildren(
            new TextElement()
                .setText(Component.translatable("container.inventory"))
                .textStyle(ts -> ts.adaptiveHeight(true)),
            new InventorySlots()
        );
    }


}

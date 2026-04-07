package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.foundation.ui.widgets.FourDirectionBlockDisplayElement;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class ProgrammableLogicGateUI extends NPUI<ProgrammableLogicGateBlockEntity> {
    public ProgrammableLogicGateUI(ProgrammableLogicGateBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.programmable_logic_gate"));
        BlockState state = blockEntity.getBlockState();
        int yRot = ((int) state.getValue(ProgrammableLogicGateBlock.FACING).toYRot() + 180) % 360;
        addChildren(
            new FourDirectionBlockDisplayElement()
                .block(state, blockEntity)
                .yRot0(yRot),
            new TextElement()
                .setText(Component.translatable("container.inventory"))
                .textStyle(ts -> ts.adaptiveHeight(true)),
            new InventorySlots()
        );
    }


}

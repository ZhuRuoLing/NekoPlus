package icu.takeneko.nekoplus.foundation.block.tile.hatch.logic;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.IContentChangeAware;
import icu.takeneko.nekoplus.block.tile.NPHatchBlockEntity;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public interface HatchLogic<C> extends ValueIOSerializable, IContentChangeAware {
    void tick();

    void onRemoved();

    C getCapabilityInstance();

    ModularUI createUI();

    static <C1, CT> C1 getCapability(NPHatchBlockEntity<C1> blockEntity, CT context) {
        return blockEntity.getHatchType().getCapability(blockEntity.getLogic());
    }

}

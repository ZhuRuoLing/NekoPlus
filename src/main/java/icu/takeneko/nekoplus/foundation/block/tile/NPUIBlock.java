package icu.takeneko.nekoplus.foundation.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import org.jetbrains.annotations.Nullable;

public interface NPUIBlock extends BlockUIMenuType.BlockUI {

    @Override
    @Nullable
    default ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        if (holder.player.level().getBlockEntity(holder.pos) instanceof Provider holder1) {
            return holder1.getModularUI(holder);
        }
        return null;
    }

    public interface Provider {
        ModularUI getModularUI(BlockUIMenuType.BlockUIHolder holder);
    }
}

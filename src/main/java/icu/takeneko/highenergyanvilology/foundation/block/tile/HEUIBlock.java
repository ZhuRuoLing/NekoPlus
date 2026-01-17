package icu.takeneko.highenergyanvilology.foundation.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;

public interface HEUIBlock extends BlockUIMenuType.BlockUI {

    @Override
    default ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        if (holder.player.level().getBlockEntity(holder.pos) instanceof IModularUIHolder holder1) {
            return holder1.getModularUI();
        }
        return null;
    }
}

package icu.takeneko.highenergyanvilology.ui.menu;

import com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder;
import icu.takeneko.highenergyanvilology.block.entity.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.ui.HEBlockUIContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class AnvilonEmitterMenu extends HEBlockUIContainerMenu<AnvilonEmitterBlockEntity> {
    public AnvilonEmitterMenu(MenuType<? extends HEBlockUIContainerMenu<?>> menuType, int windowID, Inventory inventory, IContainerUIHolder uiHolder) {
        super(menuType, windowID, inventory, uiHolder);
    }
}

package icu.takeneko.highenergyanvilology.foundation.ui;

import com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUIContainerMenu;
import icu.takeneko.highenergyanvilology.block.entity.HESynedBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class HEBlockUIContainerMenu<T extends HESynedBlockEntity> extends ModularUIContainerMenu {

    private final T blockEntity;

    @SuppressWarnings("unchecked")
    public HEBlockUIContainerMenu(
        MenuType<? extends HEBlockUIContainerMenu<?>> menuType,
        int windowID,
        Inventory inventory,
        IContainerUIHolder uiHolder
    ) {
        super((MenuType<ModularUIContainerMenu>) (Object) menuType, windowID, inventory, uiHolder);
        if (uiHolder instanceof HESynedBlockEntity) {
            blockEntity = (T) uiHolder;
        } else {
            blockEntity = null;
        }
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return super.stillValid(playerIn) && blockEntity != null;
    }
}

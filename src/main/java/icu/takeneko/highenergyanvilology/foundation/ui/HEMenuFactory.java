package icu.takeneko.highenergyanvilology.foundation.ui;

import icu.takeneko.highenergyanvilology.block.entity.HESynedBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public interface HEMenuFactory<B extends HESynedBlockEntity & BlockUIHolder, T extends HEBlockUIContainerMenu<B>> {

    T create(
        MenuType<T> menuType,
        int id,
        Inventory inventory,
        BlockUIHolder holder
    );
}

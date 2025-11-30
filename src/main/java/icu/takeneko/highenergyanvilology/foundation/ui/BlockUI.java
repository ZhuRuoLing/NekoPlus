package icu.takeneko.highenergyanvilology.foundation.ui;

import icu.takeneko.highenergyanvilology.block.entity.HESynedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockUI {

    public static <T extends HEBlockUIContainerMenu<B>, B extends HESynedBlockEntity & BlockUIHolder>
    T create(
        MenuType<T> menuType,
        int id,
        Inventory inventory,
        RegistryFriendlyByteBuf buf,
        HEMenuFactory<B, T> factory
    ) {
        Level level = inventory.player.level();
        if (buf != null) {
            BlockPos pos = buf.readBlockPos();
            BlockEntity be = level.getBlockEntity(pos);
            if (be == null) return null;
            if (!(be instanceof BlockUIHolder holder)) return null;
            T menu = factory.create(menuType, id, inventory, holder);
            menu.getSyncManager().readInitialData(buf);
            return menu;
        }
        return null;
    }
}

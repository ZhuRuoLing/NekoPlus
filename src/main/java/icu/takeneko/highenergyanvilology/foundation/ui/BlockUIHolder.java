package icu.takeneko.highenergyanvilology.foundation.ui;

import com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder;
import com.lowdragmc.lowdraglib2.gui.sync.IUISyncManagerHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface BlockUIHolder extends MenuProvider, IContainerUIHolder {

    @Override
    default void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        if (menu instanceof IUISyncManagerHolder syncManagerHolder) {
            syncManagerHolder.writeInitialData(buffer);
        }
        buffer.writeBlockPos(getBlockPos());
    }

    BlockPos getBlockPos();

    @Override
    default boolean isStillValid(Player player) {
        return player.position().distanceToSqr(getBlockPos().getCenter()) <= 16;
    }
}

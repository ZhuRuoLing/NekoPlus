package icu.takeneko.highenergyanvilology.foundation.ui;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface HEBlockEntityUIHolder extends IUIHolder {
    default BlockEntity self() {
        return (BlockEntity) this;
    }

    @Override
    default boolean isInvalid() {
        return self().isRemoved();
    }

    @Override
    default boolean isRemote() {
        return self().getLevel().isClientSide;
    }

    @Override
    default void markAsDirty() {
        self().setChanged();
    }
}

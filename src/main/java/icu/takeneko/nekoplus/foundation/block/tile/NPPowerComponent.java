package icu.takeneko.nekoplus.foundation.block.tile;

import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Objects;

public interface NPPowerComponent extends IPowerComponent {
    void setOverload(boolean value);

    boolean isOverload();

    default void flushState() {
        flushState(Objects.requireNonNull(getCurrentLevel()), getPos());
    }

    @Override
    default void flushState(Level level, BlockPos pos) {
        if (this.getGrid() == null) {
            if (!isOverload()) {
                setOverload(true);
            }
            return;
        }
        if (this.getGrid().isWorking() && isOverload()) {
            setOverload(false);
        } else if (!this.getGrid().isWorking() && !isOverload()) {
            setOverload(true);
        }
    }

    boolean isRemoved();
}


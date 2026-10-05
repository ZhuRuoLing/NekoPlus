package icu.takeneko.nekoplus.internal;

import icu.takeneko.nekoplus.foundation.block.tile.Overclockable;

public interface OverclockToggleable extends Overclockable {
    static boolean isOverclockEnabled(OverclockToggleable blockEntity) {
        return blockEntity.isOverclockEnabled();
    }

    void toggleOverclock();
}

package icu.takeneko.nekoplus.internal;

import dev.dubhe.anvilcraft.block.entity.SmartBlockPlacerBlockEntity;

public class SmartBlockPlacerBlockEntityInternals {

    public static boolean isOverclockEnabled(SmartBlockPlacerBlockEntity blockEntity) {
        return ((Extension) blockEntity).isOverclockEnabled();
    }

    public interface Extension {
        void toggleOverclock();

        boolean isOverclockEnabled();
    }
}

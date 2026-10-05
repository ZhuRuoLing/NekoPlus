package icu.takeneko.nekoplus.internal;

import dev.dubhe.anvilcraft.block.entity.SmartBlockPlacerBlockEntity;

public class SmartBlockPlacerBlockEntityInternals {

    public static boolean isOverclockEnabled(SmartBlockPlacerBlockEntity blockEntity) {
        return OverclockToggleable.isOverclockEnabled((OverclockToggleable) blockEntity);
    }

    public interface Extension extends OverclockToggleable {
    }
}

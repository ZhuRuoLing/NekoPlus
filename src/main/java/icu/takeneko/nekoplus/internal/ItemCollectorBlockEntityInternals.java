package icu.takeneko.nekoplus.internal;

import dev.dubhe.anvilcraft.block.entity.ItemCollectorBlockEntity;

public class ItemCollectorBlockEntityInternals {

    public static boolean isFilterEnabled(ItemCollectorBlockEntity blockEntity) {
        return ((Access) blockEntity).nekoplus$isFilteringEnabled();
    }

    public interface Access {
        boolean nekoplus$isFilteringEnabled();
    }
}

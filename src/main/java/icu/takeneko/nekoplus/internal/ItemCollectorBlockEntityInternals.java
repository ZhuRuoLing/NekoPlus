package icu.takeneko.nekoplus.internal;

import dev.dubhe.anvilcraft.block.entity.ItemCollectorBlockEntity;

public class ItemCollectorBlockEntityInternals {

    public static boolean isFilterEnabled(ItemCollectorBlockEntity icbe) {
        return ((Access) icbe).nekoplus$isFilteringEnabled();
    }

    public interface Access {
        boolean nekoplus$isFilteringEnabled();
    }
}

package icu.takeneko.nekoplus.item;

import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import icu.takeneko.nekoplus.foundation.item.module.type.NPEnhancementModuleType;
import lombok.Getter;
import net.minecraft.world.item.Item;

public class EnhancementModuleItem<T extends NPEnhancementModule> extends Item {
    @Getter
    private final NPEnhancementModuleType<T> moduleType;

    public EnhancementModuleItem(Properties properties, NPEnhancementModuleType<T> moduleType) {
        super(properties);
        this.moduleType = moduleType;
    }
}

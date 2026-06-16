package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class NPDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DR = DeferredRegister.create(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        NekoPlus.MODID
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<NPEnhancementModule>>> ENHANCEMENT_MODULE = DR.register(
        "enhancement_module",
        () -> DataComponentType.<List<NPEnhancementModule>>builder()
            .persistent(NPEnhancementModule.LIST_CODEC)
            .networkSynchronized(NPEnhancementModule.LIST_STREAM_CODEC)
            .cacheEncoding()
            .build()
    );
}

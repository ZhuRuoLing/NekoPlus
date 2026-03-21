package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.NPCodecs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class NPDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DR = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, NekoPlus.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ENTANGLE_ANVILON_UUID = DR.register(
        "entangle_anvilon_uuid",
        () -> DataComponentType.<UUID>builder()
            .persistent(NPCodecs.UUID_CODEC)
            .networkSynchronized(NPCodecs.UUID_STREAM_CODEC)
            .cacheEncoding()
            .build()
    );
}

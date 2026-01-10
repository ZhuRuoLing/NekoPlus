package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.foundation.HECodecs;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilMaterial;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilonType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class HEDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DR = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, HEAnvilology.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AnvilMaterial>> CONTAINED_ANVILON_TYPE = DR.register(
        "contained_anvilon_type",
        () -> DataComponentType.<AnvilMaterial>builder()
            .persistent(AnvilMaterial.CODEC)
            .networkSynchronized(AnvilMaterial.STREAM_CODEC)
            .cacheEncoding()
            .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AnvilonType.Contained>> CONTAINED_ANVILION_STATUS = DR.register(
        "contained_anvilon_status",
        () -> DataComponentType.<AnvilonType.Contained>builder()
            .persistent(AnvilonType.Contained.CODEC)
            .networkSynchronized(AnvilonType.Contained.STREAM_CODEC)
            .cacheEncoding()
            .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ENTANGLE_ANVILON_UUID = DR.register(
        "entangle_anvilon_uuid",
        () -> DataComponentType.<UUID>builder()
            .persistent(HECodecs.UUID_CODEC)
            .networkSynchronized(HECodecs.UUID_STREAM_CODEC)
            .cacheEncoding()
            .build()
    );
}

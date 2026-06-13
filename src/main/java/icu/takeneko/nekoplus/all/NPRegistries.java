package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.item.module.type.NPEnhancementModuleType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class NPRegistries {
    public static final ResourceKey<Registry<NPEnhancementModuleType<?>>> ENHANCEMENT_MODULE_TYPE_KEY = ResourceKey.createRegistryKey(
        NekoPlus.location("enhancement_module_type")
    );

    private static Registry<NPEnhancementModuleType<?>> ENHANCEMENT_MODULE_TYPE;

    @SubscribeEvent
    public static void on(NewRegistryEvent event) {
        ENHANCEMENT_MODULE_TYPE = event.create(
            new RegistryBuilder<>(ENHANCEMENT_MODULE_TYPE_KEY)
                .maxId(512)
                .sync(true)
        );
    }

    public static Registry<NPEnhancementModuleType<?>> getEnhancementModuleTypeRegistry() {
        return ENHANCEMENT_MODULE_TYPE;
    }
}

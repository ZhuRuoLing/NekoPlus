package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.foundation.material.AnvilMaterial;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber
public class NPBuiltinRegistries {

    public static final Registry<AnvilMaterial> MATERIAL = new RegistryBuilder<>(NPRegistries.MATERIAL)
        .sync(true)
        .create();

    @SubscribeEvent
    public static void on(NewRegistryEvent event) {
        event.register(MATERIAL);
    }
}

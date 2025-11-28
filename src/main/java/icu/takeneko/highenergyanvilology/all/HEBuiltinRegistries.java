package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.material.AnvilMaterial;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber
public class HEBuiltinRegistries {

    public static final Registry<AnvilMaterial> MATERIAL = new RegistryBuilder<>(HERegistries.MATERIAL)
        .sync(true)
        .create();

    @SubscribeEvent
    public static void on(NewRegistryEvent event) {
        event.register(MATERIAL);
    }
}

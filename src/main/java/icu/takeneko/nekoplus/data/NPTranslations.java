package icu.takeneko.nekoplus.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import icu.takeneko.nekoplus.all.NPBuiltinRegistries;
import icu.takeneko.nekoplus.all.NPItemTooltips;
import icu.takeneko.nekoplus.foundation.material.AnvilMaterial;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public class NPTranslations {
    public static void addTranslations(RegistrateLangProvider provider) {
        NPItemTooltips.setupTooltips();
        provider.add("tooltip.nekoplus.particle_stabilizer.cooldown", "Cooldown: %s sec");
        provider.add("tooltip.nekoplus.particle_stabilizer.cooling", "Cooling");
        provider.add("tooltip.nekoplus.particle_stabilizer.ready", "Ready");
        provider.add("tooltip.nekoplus.particle_stabilizer.state", "State: ");
        provider.add("config.jade.plugin_nekoplus.particle_stabilizer", "Particle Stabilizer");

        provider.add("item.nekoplus.magnetic_confinement_vessel.full", "Magnetic Confinement Vessel with %s");
        provider.add("anvilon.nekoplus.status.unstable", "Unstable %s");
        provider.add("anvilon.nekoplus.status.entangled", "Entangled %s");
        provider.add("anvilon.nekoplus.status.stable", "Stable %s");
        for (Map.Entry<ResourceKey<AnvilMaterial>, AnvilMaterial> anvilMaterial : NPBuiltinRegistries.MATERIAL.entrySet()) {
            ResourceKey<AnvilMaterial> key = anvilMaterial.getKey();
            provider.add(
                "anvilon." + key.location().getNamespace() + ".type." + key.location().getPath(),
                RegistrateLangProvider.toEnglishName(key.location().getPath()) + " Anvilon"
            );
        }
    }
}

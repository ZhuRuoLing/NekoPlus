package icu.takeneko.highenergyanvilology.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import icu.takeneko.highenergyanvilology.all.HEBuiltinRegistries;
import icu.takeneko.highenergyanvilology.all.HEItemTooltips;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilMaterial;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public class HETranslations {
    public static void addTranslations(RegistrateLangProvider provider) {
        HEItemTooltips.setupTooltips();
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.cooldown", "Cooldown: %s sec");
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.cooling", "Cooling");
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.ready", "Ready");
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.state", "State: ");
        provider.add("config.jade.plugin_highenergyanvilology.particle_stabilizer", "Particle Stabilizer");

        provider.add("item.highenergyanvilology.magnetic_confinement_vessel.full", "Magnetic Confinement Vessel with %s");
        provider.add("anvilon.highenergyanvilology.status.unstable", "Unstable %s");
        provider.add("anvilon.highenergyanvilology.status.entangled", "Entangled %s");
        provider.add("anvilon.highenergyanvilology.status.stable", "Stable %s");
        for (Map.Entry<ResourceKey<AnvilMaterial>, AnvilMaterial> anvilMaterial : HEBuiltinRegistries.MATERIAL.entrySet()) {
            ResourceKey<AnvilMaterial> key = anvilMaterial.getKey();
            provider.add(
                "anvilon." + key.location().getNamespace() + ".type." + key.location().getPath(),
                RegistrateLangProvider.toEnglishName(key.location().getPath()) + " Anvilon"
            );
        }
    }
}

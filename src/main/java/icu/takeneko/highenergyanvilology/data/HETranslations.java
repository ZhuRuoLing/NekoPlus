package icu.takeneko.highenergyanvilology.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import icu.takeneko.highenergyanvilology.all.HEItemTooltips;

public class HETranslations {
    public static void addTranslations(RegistrateLangProvider provider) {
        HEItemTooltips.setupTooltips();
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.cooldown", "Cooldown: %s sec");
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.cooling", "Cooling");
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.ready", "Ready");
        provider.add("tooltip.highenergyanvilology.particle_stabilizer.state", "State: ");
        provider.add("config.jade.plugin_highenergyanvilology.particle_stabilizer", "Particle Stabilizer");
    }
}

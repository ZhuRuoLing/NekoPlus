package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumLangProvider;
import icu.takeneko.nekoplus.all.NPBuiltinRegistries;
import icu.takeneko.nekoplus.all.NPItemTooltips;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public class NPTranslations {
    public static void addTranslations(RegistrumLangProvider provider) {
        NPItemTooltips.setupTooltips();
        provider.add("tooltip.nekoplus.particle_stabilizer.cooldown", "Cooldown: %s sec");
        provider.add("tooltip.nekoplus.particle_stabilizer.cooling", "Cooling");
        provider.add("tooltip.nekoplus.particle_stabilizer.ready", "Ready");
        provider.add("tooltip.nekoplus.particle_stabilizer.state", "State: ");
        provider.add("config.jade.plugin_nekoplus.particle_stabilizer", "Particle Stabilizer");
        provider.add("config.jade.plugin_nekoplus.overclockable", "Overclocking");

        provider.add("item.nekoplus.magnetic_confinement_vessel.full", "Magnetic Confinement Vessel with %s");

        provider.add("tooltip.nekoplus.overclock.enabled", "§bOverclock: §a§lON");
        provider.add("tooltip.nekoplus.overclock.disabled", "§bOverclock: §e§lOFF");
        provider.add("tooltip.nekoplus.overclock.ratio", "OC: %s/%s");

        provider.add("subtitles.block.particle_stabilizer.working", "Particle Stabilizer: Working");

        provider.add("category.nekoplus.air_condensing", "Air Condensing");
        provider.add("category.nekoplus.air_condensing.dimension", "Dimension: %s");
        provider.add("category.nekoplus.laser_etching", "Laser Etching");
        provider.add("category.nekoplus.laser_etching.laser_requirement", "Requires Laser Strength >= 64");

        provider.add("evaluator.undefined_symbol", "Tried to access undefined symbol %s or its value has not been set.");
    }
}

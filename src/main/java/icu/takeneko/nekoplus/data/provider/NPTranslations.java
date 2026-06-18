package icu.takeneko.nekoplus.data.provider;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumLangProvider;
import icu.takeneko.nekoplus.all.NPItemTooltips;

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

        provider.add("ui.programmable_logic_gate.red", "Configure Pin: Red");
        provider.add("ui.programmable_logic_gate.green", "Configure Pin: Green");
        provider.add("ui.programmable_logic_gate.blue", "Configure Pin: Blue");
        provider.add("ui.programmable_logic_gate.white", "Configure Pin: White");
        provider.add("ui.programmable_logic_gate.pin_mode", "Pin Mode ");
        provider.add("ui.programmable_logic_gate.expression", "Expression ");

        provider.add("evaluator.undefined_symbol", "Tried to use undeclared identifier %s or its value has not been set.");
        provider.add("evaluator.inspection.undefined_symbol", "§cerror:§r use of undeclared identifier '%s' at line %s: %s <--[HERE]");
        provider.add("evaluator.inspection.summary_pl", "%s errors generated.");
        provider.add("evaluator.inspection.summary", "1 error generated.");
        provider.add("evaluator.inspection.unexpected_token", "§cerror:§r unexpected token: ...%s <-- [HERE]");
        provider.add("evaluator.inspection.expect_rparen", "§cerror:§r expected ')'");
        provider.add("evaluator.inspection.not_assignable", "§cerror:§r expression is not assignable: ...%s <-- [HERE]");

        provider.add("tooltip.nekoplus.modular_enhancement_template.applies_to", "Any Equipments");
        provider.add("tooltip.nekoplus.modular_enhancement_template.base_slot_description", "Add any armor, weapon, or tool");
        provider.add("tooltip.nekoplus.modular_enhancement_template.additions_slot_description", "Add Advanced Processor");

        provider.add("tooltip.nekoplus.enhancement_module.no_module", "No enhancement module installed");
        provider.add("tooltip.nekoplus.enhancement_module.enhancement_modules", "Enhancement Modules:");

        provider.add("tooltip.nekoplus.enhancement_module.slot.any", "Any Item");
        provider.add("tooltip.nekoplus.enhancement_module.slot.mainhand", "Any Item");
        provider.add("tooltip.nekoplus.enhancement_module.slot.offhand", "Any Item in Offhand");
        provider.add("tooltip.nekoplus.enhancement_module.slot.hand", "Any Item in Mainhand");
        provider.add("tooltip.nekoplus.enhancement_module.slot.feet", "Boots");
        provider.add("tooltip.nekoplus.enhancement_module.slot.legs", "Leggings");
        provider.add("tooltip.nekoplus.enhancement_module.slot.chest", "Chestplate");
        provider.add("tooltip.nekoplus.enhancement_module.slot.head", "Helmet");
        provider.add("tooltip.nekoplus.enhancement_module.slot.armor", "Any Body Armor");
        provider.add("tooltip.nekoplus.enhancement_module.slot.body", "Any Armor");
        provider.add("tooltip.nekoplus.enhancement_module.slot.saddle", "Saddle");

        provider.add("tooltip.nekoplus.enhancement_module.anti_gravity.name", "Anti Gravity");
        provider.add("tooltip.nekoplus.titanium_crystal_module.desc", "Increases attack damage by 30%");
        provider.add("tooltip.nekoplus.mechanical_heart.desc", "Increases max health by 30%");

        provider.add("tooltip.format.indent_list", "  %s");
        provider.add("tooltip.format.indent", "    %s");
    }
}

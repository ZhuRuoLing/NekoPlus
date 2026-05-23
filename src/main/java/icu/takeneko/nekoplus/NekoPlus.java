package icu.takeneko.nekoplus;

import dev.anvilcraft.lib.v2.registrum.Registrum;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPCreativeTabs;
import icu.takeneko.nekoplus.all.NPDataComponents;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.all.NPSoundEvents;
import icu.takeneko.nekoplus.config.NPConfig;
import icu.takeneko.nekoplus.data.NPDataGen;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(NekoPlus.MODID)
public class NekoPlus {
    public static final String MODID = "nekoplus";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Registrum REGISTRUM = Registrum.create(MODID);

    public NekoPlus(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, NPConfig.SPEC);

        setupRegistration(modEventBus);
        NPDataGen.setupDataGeneration(REGISTRUM);
    }

    private void setupRegistration(IEventBus modEventBus) {
        NPBlocks.setupRegistration();
        NPBlockEntities.setupRegistration();
        NPItems.setupRegistration();
        NPCreativeTabs.DR.register(modEventBus);
        NPDataComponents.DR.register(modEventBus);
        NPSoundEvents.DR.register(modEventBus);
        NPRecipeTypes.DR.register(modEventBus);
        NPRecipeTypes.RECIPE_SERIALIZER_DR.register(modEventBus);
    }

    public static Identifier location(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}

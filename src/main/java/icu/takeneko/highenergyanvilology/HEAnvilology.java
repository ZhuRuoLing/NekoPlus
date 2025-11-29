package icu.takeneko.highenergyanvilology;

import com.tterrag.registrate.Registrate;
import icu.takeneko.highenergyanvilology.all.HEAnvilMaterials;
import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.all.HEBlocks;
import icu.takeneko.highenergyanvilology.all.HECreativeTabs;
import icu.takeneko.highenergyanvilology.all.HEItems;
import icu.takeneko.highenergyanvilology.config.HEConfig;
import icu.takeneko.highenergyanvilology.data.HEDataGen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod(HEAnvilology.MODID)
public class HEAnvilology {
    public static final String MODID = "highenergyanvilology";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Registrate REGISTRATE = Registrate.create(MODID);

    public HEAnvilology(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, HEConfig.SPEC);

        setupRegistration(modEventBus);
        HEDataGen.setupDataGeneration(REGISTRATE);
    }

    private void setupRegistration(IEventBus modEventBus) {
        HEBlocks.setupRegistration();
        HEBlockEntities.setupRegistration();
        HEItems.setupRegistration();
        HEAnvilMaterials.DR.register(modEventBus);
        HECreativeTabs.DR.register(modEventBus);
    }

    @SubscribeEvent
    private void commonSetup(FMLCommonSetupEvent event) {

    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}

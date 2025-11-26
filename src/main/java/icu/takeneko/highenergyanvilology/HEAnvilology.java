package icu.takeneko.highenergyanvilology;

import icu.takeneko.highenergyanvilology.config.HEConfig;
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

    public HEAnvilology(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, HEConfig.SPEC);
    }

    @SubscribeEvent
    private void commonSetup(FMLCommonSetupEvent event) {

    }
}

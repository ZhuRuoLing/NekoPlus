package icu.takeneko.highenergyanvilology.client;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.all.HEHammerTooltipProviders;
import icu.takeneko.highenergyanvilology.foundation.block.entity.SpecialRendererBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.ArrayList;
import java.util.List;


@Mod(value = HEAnvilology.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class HEAnvilologyClient {

    public HEAnvilologyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        //container.registerExtensionPoint(IClientItemExtensions.class, new HEClientExtension());
        HEHammerTooltipProviders.setupRegistration();
    }

    @SubscribeEvent
    public static void on(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void on(RegisterClientExtensionsEvent event) {
        List<Item> bewlrItem = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof SpecialRendererBlock) {
                bewlrItem.add(item);
            }
        });
        event.registerItem(new HEClientExtension(bewlrItem), bewlrItem.reversed().toArray(new Item[0]));
    }

}

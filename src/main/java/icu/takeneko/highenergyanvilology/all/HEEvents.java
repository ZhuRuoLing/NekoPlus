package icu.takeneko.highenergyanvilology.all;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUIContainerScreen;
import icu.takeneko.highenergyanvilology.foundation.ui.client.HEModularUIContainerScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber
public class HEEvents {
    @SubscribeEvent
    public static void on(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            HEBlockEntities.ANVILION_EMITTER.get(),
            (a, v) -> a.getItemHandler()
        );
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void on(RegisterMenuScreensEvent event) {
            event.register(HEMenuTypes.ANVILON_EMITTER.get(), HEModularUIContainerScreen::new);
        }
    }
}

package icu.takeneko.highenergyanvilology.util;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientTimer {
    private static int ticks = 0;

    @SubscribeEvent
    public static void on(ClientTickEvent.Pre event) {
        ticks++;
    }

    public static int getClientTime() {
        return ticks;
    }
}

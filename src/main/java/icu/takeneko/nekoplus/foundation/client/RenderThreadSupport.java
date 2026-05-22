package icu.takeneko.nekoplus.foundation.client;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderFrameEvent;

import java.util.ArrayDeque;
import java.util.Deque;

@EventBusSubscriber
public class RenderThreadSupport {

    private static final Deque<Runnable> tasks = new ArrayDeque<>();

    public static void recordRenderCall(Runnable call) {
        if (Minecraft.getInstance().isSameThread()) {
            call.run();
        }
        tasks.add(call);
    }

    @SubscribeEvent
    public static void on(RenderFrameEvent.Pre event) {
        synchronized (tasks) {
            for (Runnable task : tasks) {
                task.run();
            }
        }
    }
}

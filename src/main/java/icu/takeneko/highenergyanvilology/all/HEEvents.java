package icu.takeneko.highenergyanvilology.all;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import icu.takeneko.highenergyanvilology.foundation.block.entity.BlockCollisionEventReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
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
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            HEBlockEntities.PARTICLE_STABILIZER.get(),
            (a, v) -> {
                if (v == null) return a.getItemHandler();
                if (v == Direction.DOWN) return a.getItemHandler().slice(1, 2, true);
                return a.getItemHandler().slice(0, 1);
            }
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void on(AnvilEvent.CollisionBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof BlockCollisionEventReceiver receiver) {
            if (receiver.acceptCollision(event.getEntity(), event.getSpeed(), event)) {
                event.setCanceled(true);
            }
        }
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void on(RegisterMenuScreensEvent event) {
        }
    }
}

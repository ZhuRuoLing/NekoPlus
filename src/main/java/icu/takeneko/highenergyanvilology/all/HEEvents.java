package icu.takeneko.highenergyanvilology.all;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import icu.takeneko.highenergyanvilology.foundation.block.entity.BlockCollisionEventReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    //AnvilEvent.CollisionBlock is not cancellable so a mixin were used to achieve this
    //@SubscribeEvent(priority = EventPriority.HIGH)
    public static boolean on(AnvilEvent.CollisionBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof BlockCollisionEventReceiver receiver) {
            return receiver.acceptCollision(event.getEntity(), event.getSpeed(), event);
        }
        return false;
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void on(RegisterMenuScreensEvent event) {
        }
    }
}

package icu.takeneko.highenergyanvilology.all;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import icu.takeneko.highenergyanvilology.client.renderer.bewlr.MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer;
import icu.takeneko.highenergyanvilology.foundation.block.entity.BlockCollisionEventReceiver;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilonType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
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
        if (level instanceof ClientLevel) return;
        BlockPos pos = event.getPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof BlockCollisionEventReceiver receiver) {
            if (receiver.acceptCollision(event.getEntity(), event.getSpeed(), event)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void on(FMLCommonSetupEvent event) {
        event.enqueueWork(HEItemTooltips::setupTooltips);
        event.enqueueWork(AnvilonType::handleRegistration);
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void on(RegisterMenuScreensEvent event) {
        }

        @SubscribeEvent
        public static void on(ModelEvent.RegisterAdditional event) {
            event.register(MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer.CONTAINER_MODEL_LOCATION);
            event.register(MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer.CONTENT_MODEL_LOCATION);
            event.register(MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer.MAGNETIC_MODEL_LOCATION);
        }

        @SubscribeEvent
        public static void on(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> {
                    if (tintIndex == 0) {
                        return FastColor.ARGB32.opaque(stack.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE.get(), HEAnvilMaterials.EMPTY).color());
                    }
                    return -1;
                },
                HEItems.MAGNETIC_CONFINEMENT_VESSEL
            );
        }
    }
}

package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.api.event.BlockEntityEvent;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.event.ServerBlockEntityEventListener;
import icu.takeneko.nekoplus.all.NPCapabilities;
import icu.takeneko.nekoplus.foundation.block.tile.NPPowerComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerBlockEntityEventListener.class)
public class ServerBlockEntityEventListenerMixin {
    @Inject(
        method = "onLoad",
        at = @At("HEAD")
    )
    private static void handlePowerCapabilitiesLoad(BlockEntityEvent.ServerLoad event, CallbackInfo ci) {
        BlockEntity entity = event.getEntity();
        Level level = entity.getLevel();
        if (level == null) return;
        NPPowerComponent capability = NPCapabilities.POWER_COMPONENT.getCapability(level, entity.getBlockPos(), entity.getBlockState(), entity, null);
        if (capability != null) {
            PowerGrid.addComponent(capability);
        }
    }

    @Inject(
        method = "onUnload",
        at = @At("HEAD")
    )
    private static void handlePowerCapabilitiesUnload(BlockEntityEvent.ServerUnload event, CallbackInfo ci) {
        BlockEntity entity = event.getEntity();
        Level level = entity.getLevel();
        if (level == null) return;
        NPPowerComponent capability = NPCapabilities.POWER_COMPONENT.getCapability(level, entity.getBlockPos(), entity.getBlockState(), entity, null);
        if (capability != null) {
            PowerGrid.removeComponent(capability);
        }
    }
}

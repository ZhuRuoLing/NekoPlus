package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.network.LaserEmitPacket;
import icu.takeneko.highenergyanvilology.internal.LaserRendererInternals;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LaserEmitPacket.class)
public class LaserEmitPacketMixin implements LaserRendererInternals.PacketAccess {
    @Unique
    private boolean he$isPureHELaser;

    @Inject(
        method = "<init>(Lnet/minecraft/network/RegistryFriendlyByteBuf;)V",
        at = @At("RETURN")
    )
    void heReadInjectedValue(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
        he$isPureHELaser = buf.readBoolean();
    }

    @Inject(
        method = "encode",
        at = @At("RETURN")
    )
    void heWriteInjectedValue(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeBoolean(he$isPureHELaser);
    }

    @Inject(
        method = "lambda$clientHandler$0",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;clientUpdate(Lnet/minecraft/core/BlockPos;I)V")
    )
    private static void handleClientUpdate(
        LaserEmitPacket data,
        CallbackInfo ci,
        @Local BaseLaserBlockEntity be
    ) {
        LaserRendererInternals.PacketAccess access = (LaserRendererInternals.PacketAccess) (data);
        ((LaserRendererInternals.Extension) be).setPureHELaserSourceDirect(access.isPureHELaserSource());
    }

    @Override
    public boolean isPureHELaserSource() {
        return he$isPureHELaser;
    }

    @Override
    public void setPureHELaserSource(boolean value) {
        he$isPureHELaser = value;
    }
}

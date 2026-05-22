package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Function3;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.network.LaserEmitPacket;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(LaserEmitPacket.class)
public class LaserEmitPacketMixin implements LaserRendererInternals.PacketAccess {
    @Unique
    private boolean he$isPureHELaser;

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/StreamCodec;"
        )
    )
    private static <T1, T2, T3> StreamCodec<? super ByteBuf, LaserEmitPacket> modifyStreamCodec(
        StreamCodec<? super ByteBuf, T1> codec1,
        Function<LaserEmitPacket, T1> getter1,
        StreamCodec<? super ByteBuf, T2> codec2,
        Function<LaserEmitPacket, T2> getter2,
        StreamCodec<? super ByteBuf, T3> codec3,
        Function<LaserEmitPacket, T3> getter3,
        Function3<T1, T2, T3, LaserEmitPacket> constructor,
        Operation<StreamCodec<? super ByteBuf, LaserEmitPacket>> original
    ) {
        return StreamCodec.composite(
            codec1,
            getter1,
            codec2,
            getter2,
            codec3,
            getter3,
            ByteBufCodecs.BOOL,
            it -> ((LaserRendererInternals.PacketAccess) (Object) (it)).isPureHELaserSource(),
            (t1, t2, t3, b) -> {
                LaserEmitPacket packet = constructor.apply(t1, t2, t3);
                ((LaserRendererInternals.PacketAccess) (Object) (packet)).setPureHELaserSource(b);
                return packet;
            }
        );
    }

    @Inject(
        method = "handleOnClient",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;clientUpdate(Lnet/minecraft/core/BlockPos;I)V")
    )
    private void handleClientUpdate(
        Player player,
        CallbackInfo ci,
        @Local(name = "laser") BaseLaserBlockEntity be
    ) {
        ((LaserRendererInternals.Extension) be).setPureHELaserSourceDirect(this.isPureHELaserSource());
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

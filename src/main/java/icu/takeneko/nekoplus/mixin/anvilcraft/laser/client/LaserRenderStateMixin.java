package icu.takeneko.nekoplus.mixin.anvilcraft.laser.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.client.renderer.blockentity.state.LaserRenderState;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LaserRenderState.class)
public class LaserRenderStateMixin implements LaserRendererInternals.RenderStateAccess {

    @Unique
    private boolean np$isPureHELaser;

    @Override
    public boolean isPureHELaserSource() {
        return this.np$isPureHELaser;
    }

    @Inject(
        method = "extract",
        at = @At("HEAD")
    )
    void extractNPStates(BaseLaserBlockEntity blockEntity, CallbackInfo ci) {
        this.np$isPureHELaser = LaserRendererInternals.hasPureHELaserSource(blockEntity);
    }

    @WrapOperation(
        method = "extract",
        at = @At(
            value = "FIELD",
            target = "Ldev/dubhe/anvilcraft/client/renderer/blockentity/state/LaserRenderState;coreColor:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    void modifyCoreColorIfHELaser(
        LaserRenderState instance,
        int value,
        Operation<Void> original,
        @Local(argsOnly = true) BaseLaserBlockEntity blockEntity
    ) {
        if (this.np$isPureHELaser) {
            original.call(instance, blockEntity.getLaserColor());
            return;
        }
        original.call(instance, value);
    }
}

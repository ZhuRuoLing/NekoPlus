package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.client.renderer.blockentity.state.LaserRenderState;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
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
        at = @At("RETURN")
    )
    void extractNPStates(BaseLaserBlockEntity blockEntity, CallbackInfo ci) {
        this.np$isPureHELaser = LaserRendererInternals.hasPureHELaserSource(blockEntity);
    }
}

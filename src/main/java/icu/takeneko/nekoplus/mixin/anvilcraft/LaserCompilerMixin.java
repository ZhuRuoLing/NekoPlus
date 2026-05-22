package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.client.renderer.blockentity.state.LaserRenderState;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserCompiler;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LaserCompiler.class)
public class LaserCompilerMixin {
    @Shadow
    @Final
    public static float[] LASER_WIDTH;

    @Inject(
        method = "laserWidth",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void handleHELaser(LaserRenderState state, CallbackInfoReturnable<Float> cir){
        if (state instanceof LaserRendererInternals.RenderStateAccess renderStateAccess && renderStateAccess.isPureHELaserSource()) {
            cir.setReturnValue(LASER_WIDTH[Math.clamp(state.laserLevel / 64, 1, 64)] + 0.001F);
            cir.cancel();
        }
    }
}

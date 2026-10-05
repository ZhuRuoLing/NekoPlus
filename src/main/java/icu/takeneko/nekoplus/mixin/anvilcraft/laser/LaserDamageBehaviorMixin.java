package icu.takeneko.nekoplus.mixin.anvilcraft.laser;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.anvilcraft.api.laser.ILaserComponentOwner;
import dev.dubhe.anvilcraft.api.laser.LaserDamageBehavior;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LaserDamageBehavior.class)
public class LaserDamageBehaviorMixin {
    @WrapOperation(
        method = "onHitBlock",
        at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I")
    )
    int modifyMaxHurt(
        int a,
        int b,
        Operation<Integer> original,
        @Local(argsOnly = true) ILaserComponentOwner owner
    ) {
        boolean pureHELaserSource = LaserRendererInternals.hasPureHELaserSource(owner);
        return original.call(pureHELaserSource ? a * 64 : a, b);
    }
}

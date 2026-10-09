package icu.takeneko.nekoplus.mixin.anvilcraft.laser;

import dev.dubhe.anvilcraft.api.laser.ILaserComponentOwner;
import dev.dubhe.anvilcraft.api.laser.LaserMiningComponent;
import dev.dubhe.anvilcraft.api.laser.LaserParticleBehavior;
import dev.dubhe.anvilcraft.util.BlockMiningEffect;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import net.minecraft.core.particles.DustParticleOptions;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LaserParticleBehavior.class)
public class LaserParticleBehaviorMixin {
    @Shadow
    private @Nullable DustParticleOptions dust;

    @Shadow
    private int dustColor;

    @Inject(
        method = "dustFor",
        at = @At("HEAD"),
        cancellable = true
    )
    private void modifyDustColor(
        ILaserComponentOwner owner,
        CallbackInfoReturnable<DustParticleOptions> cir
    ) {
        boolean pure = LaserRendererInternals.hasPureHELaserSource(owner);
        if (!pure) return;
        BlockMiningEffect effect = LaserMiningComponent.getEffect(owner);
        int color = effect == BlockMiningEffect.NORMAL
            ? HighEnergyLaserBlockEntity.HIGH_ENERGY_LASER_COLOR
            : effect.getLaserColor();

        if (this.dust == null || this.dustColor != color) {
            this.dustColor = color;
            this.dust = new DustParticleOptions(color, 1f);
        }
        cir.setReturnValue(this.dust);
    }
}

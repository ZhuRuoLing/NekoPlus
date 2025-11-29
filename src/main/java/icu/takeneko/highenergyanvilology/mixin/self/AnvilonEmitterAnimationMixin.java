package icu.takeneko.highenergyanvilology.mixin.self;

import dev.dubhe.anvilcraft.util.WatchableCyclingValue;
import icu.takeneko.highenergyanvilology.block.entity.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.client.renderer.helper.AnimationDataConfigurator;
import io.github.tt432.eyelib.molang.MolangScope;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnvilonEmitterBlockEntity.class)
public class AnvilonEmitterAnimationMixin implements AnimationDataConfigurator {

    @Shadow
    @Final
    private WatchableCyclingValue<Float> rate;

    @Override
    public void configureMolangScope(MolangScope scope) {
        scope.set("variable.animation", rate.get() > 0 ? 1 : 0);
    }

    @Override
    public float getAnimationRateMultiplier() {
        return Math.max(rate.get(), 1);
    }
}

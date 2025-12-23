package icu.takeneko.highenergyanvilology.mixin.self;

import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.client.renderer.helper.AnimationDataConfigurator;
import io.github.tt432.eyelib.molang.MolangScope;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnvilonEmitterBlockEntity.class)
public class AnvilonEmitterAnimationMixin implements AnimationDataConfigurator {

    @Shadow
    private boolean isOverload;

    @Override
    public void configureMolangScope(MolangScope scope) {
        scope.set("variable.overload", this.isOverload ? 1 : 0);
    }
}

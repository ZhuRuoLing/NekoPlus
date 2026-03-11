package icu.takeneko.nekoplus.mixin.self;

import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.client.renderer.helper.AnimationDataConfigurator;
import io.github.tt432.eyelib.molang.MolangScope;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ParticleStabilizerBlockEntity.class)
public class ParticleStabilizerBlockEntityMixin implements AnimationDataConfigurator {
    @Shadow
    private boolean isOverload;

    @Shadow private ParticleStabilizerBlockEntity.State state;

    @Override
    public void configureMolangScope(MolangScope scope) {
        scope.set("variable.overload", this.isOverload ? 1 : 0);
        scope.set("variable.frozen", this.state == ParticleStabilizerBlockEntity.State.WORKING ? 1 : 0);
    }
}

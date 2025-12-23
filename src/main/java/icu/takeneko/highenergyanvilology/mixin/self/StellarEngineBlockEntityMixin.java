package icu.takeneko.highenergyanvilology.mixin.self;

import icu.takeneko.highenergyanvilology.block.tile.StellarEngineBlockEntity;
import icu.takeneko.highenergyanvilology.client.renderer.helper.AnimationDataConfigurator;
import io.github.tt432.eyelib.molang.MolangScope;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StellarEngineBlockEntity.class)
public class StellarEngineBlockEntityMixin implements AnimationDataConfigurator {

    @Override
    public void configureMolangScope(MolangScope scope) {
        AnimationDataConfigurator.super.configureMolangScope(scope);
        scope.set("variable.open", 1);
        scope.set("variable.not_closed", 1);
        scope.set("variable.rotating_speed", 1);
    }
}

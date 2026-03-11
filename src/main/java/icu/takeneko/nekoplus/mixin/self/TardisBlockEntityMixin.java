package icu.takeneko.nekoplus.mixin.self;

import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;
import icu.takeneko.nekoplus.client.renderer.helper.AnimationDataConfigurator;
import io.github.tt432.eyelib.molang.MolangScope;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TardisBlockEntity.class)
public class TardisBlockEntityMixin implements AnimationDataConfigurator {


    @Shadow
    private boolean animation;

    @Override
    public void configureMolangScope(MolangScope scope) {
        AnimationDataConfigurator.super.configureMolangScope(scope);
        
        scope.set("prop.rotating.speed", 1);

        scope.set("prop.floating.delta_height", 4);
        scope.set("prop.floating.min_height", 4);
        scope.set("prop.floating.speed", 0.3f);

        scope.set("prop.waving.delta_deg", 10);
        scope.set("prop.waving.speed", 0.25f);

        scope.set("prop.flashing.speed", 0.75f);
        scope.set("prop.flashing.size", 1.5f);

        if (animation) {
            scope.set("action.floating", 1);
            scope.set("action.rotating", 1);
            scope.set("action.waving", 1);
        } else {
            scope.set("action.floating", 0);
            scope.set("action.rotating", 0);
            scope.set("action.waving", 0);
        }
    }
}

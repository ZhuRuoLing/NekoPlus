package icu.takeneko.highenergyanvilology.client.renderer.helper;

import io.github.tt432.eyelib.molang.MolangScope;

public interface AnimationDataConfigurator {
    AnimationDataConfigurator DEFAULT = new AnimationDataConfigurator() {
    };

    default void configureMolangScope(MolangScope scope) {
    }

    default float getAnimationRateMultiplier() {
        return 1f;
    }
}

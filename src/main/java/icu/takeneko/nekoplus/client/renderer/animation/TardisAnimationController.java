package icu.takeneko.nekoplus.client.renderer.animation;

import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;

public class TardisAnimationController extends AnimationController<TardisBlockEntity> {
    public static final RawAnimation ROTATING = RawAnimation.begin()
        .thenLoop("nekoplus:animation.tardis.rotating1");

    public static final RawAnimation WAVING = RawAnimation.begin()
        .thenLoop("nekoplus:animation.tardis.waving");

    private RawAnimation animation;

    public TardisAnimationController(String name, AnimationStateHandler<TardisBlockEntity> handler) {
        super(name, handler);
    }

    public static TardisAnimationController createForAnimation(String name, RawAnimation rawAnimation) {
        TardisAnimationController controller = new TardisAnimationController(name, TardisAnimationController::handle);
        controller.animation = rawAnimation;
        return controller;
    }

    private static PlayState handle(AnimationTest<TardisBlockEntity> event) {
        if (event.controller() instanceof TardisAnimationController controller) {
            if (event.animatable().isAnimating()) {
                return event.setAndContinue(controller.animation);
            }
            event.controller().reset();
            return PlayState.STOP;
        }
        return PlayState.STOP;
    }
}

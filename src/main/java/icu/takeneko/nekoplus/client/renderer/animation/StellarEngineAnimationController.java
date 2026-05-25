package icu.takeneko.nekoplus.client.renderer.animation;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.stateless.StatelessAnimationController;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import icu.takeneko.nekoplus.block.tile.StellarEngineBlockEntity;

public class StellarEngineAnimationController extends AnimationController<StellarEngineBlockEntity> {

    public static final RawAnimation ANIM_CLOSED = RawAnimation.begin()
        .thenPlayAndHold("nekoplus:animation.stellar_engine.closed");
    public static final RawAnimation ANIM_OPEN = RawAnimation.begin()
        .thenPlay("nekoplus:animation.stellar_engine.open");
    public static final RawAnimation ANIM_RING = RawAnimation.begin()
        .thenLoop("nekoplus:animation.stellar_engine.ring");
    public static final RawAnimation ANIM_SUN = RawAnimation.begin()
        .thenLoop("nekoplus:animation.stellar_engine.sun");

    public StellarEngineAnimationController(AnimationStateHandler<StellarEngineBlockEntity> handler) {
        super("stellar_engine", handler);
    }

    public static StellarEngineAnimationController openCloseController() {
        return new StellarEngineAnimationController(StellarEngineAnimationController::handleOpenCloseState);
    }

    public static StellarEngineAnimationController ringAnimation() {
        return new StellarEngineAnimationController(it -> StellarEngineAnimationController.handleOpened(it, ANIM_RING));
    }

    public static StellarEngineAnimationController sunAnimation() {
        return new StellarEngineAnimationController(it -> StellarEngineAnimationController.handleOpened(it, ANIM_SUN));
    }

    private static PlayState handleOpenCloseState(AnimationTest<StellarEngineBlockEntity> event) {
        if (!(event.controller() instanceof StellarEngineAnimationController)) return PlayState.CONTINUE;
        StellarEngineBlockEntity blockEntity = event.animatable();
        StellarEngineBlockEntity.EngineAnimationState state = blockEntity.getEngineAnimationState();

        return switch (state) {
            case CLOSED -> event.setAndContinue(ANIM_CLOSED);
            case OPENING -> {
                yield event.setAndContinue(ANIM_OPEN);
//                blockEntity.setEngineAnimationState(StellarEngineBlockEntity.EngineAnimationState.OPENED);
//                yield PlayState.STOP;
            }
            case OPENED -> PlayState.STOP;
        };
    }

    private static PlayState handleOpened(AnimationTest<StellarEngineBlockEntity> event, RawAnimation animation) {
        if (!(event.controller() instanceof StellarEngineAnimationController)) return PlayState.CONTINUE;
        StellarEngineBlockEntity blockEntity = event.animatable();
        StellarEngineBlockEntity.EngineAnimationState state = blockEntity.getEngineAnimationState();

        return switch (state) {
            case CLOSED, OPENED -> PlayState.STOP;
            case OPENING -> {
                event.setAnimation(animation);
                yield PlayState.CONTINUE;
            }
        };
    }

    public static AnimationController<GeoAnimatable> createStateless(String name, RawAnimation animation) {
        StatelessAnimationController controller = new StatelessAnimationController(name);
        controller.setCurrentAnimation(animation);
        return controller;
    }
}

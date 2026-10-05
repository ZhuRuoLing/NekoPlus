package icu.takeneko.nekoplus.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class StampingPlatformsInternals {
    public static final BooleanProperty LASER_TARGETED = BooleanProperty.create("laser_targeted");


    public interface LaserTarget {
        void nekoplus$setEmitter(BlockPos emitter, boolean active);

        void nekoplus$tick();
    }
}

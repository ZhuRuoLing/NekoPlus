package icu.takeneko.highenergyanvilology.foundation.block.entity;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import net.minecraft.world.entity.item.FallingBlockEntity;

public interface BlockCollisionEventReceiver {
    boolean acceptCollision(FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event);
}

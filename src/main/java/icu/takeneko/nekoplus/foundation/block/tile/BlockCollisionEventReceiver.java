package icu.takeneko.nekoplus.foundation.block.tile;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import net.minecraft.world.entity.item.FallingBlockEntity;

public interface BlockCollisionEventReceiver {
    boolean acceptCollision(FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event);
}

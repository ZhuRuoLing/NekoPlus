package icu.takeneko.nekoplus.content.tile.logic.stabilizer;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;

public interface ParticleStabilizerLogic {

    boolean isValidTriggerItem(ItemResource stack);

    boolean tryTrigger(ParticleStabilizerLogicHost host);

    void tick(ParticleStabilizerLogicHost host);

    boolean handleCollision(ParticleStabilizerLogicHost host, FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event);

    void deactivate(ParticleStabilizerLogicHost host);

    final class Impl implements ParticleStabilizerLogic {

        private final ParticleStabilizerLogic[] logics = ParticleStabilizerLogics.values();

        @Override
        public boolean isValidTriggerItem(ItemResource stack) {
            for (ParticleStabilizerLogic logic : logics) {
                if (logic.isValidTriggerItem(stack)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean tryTrigger(ParticleStabilizerLogicHost host) {
            return true;
        }

        @Override
        public void tick(ParticleStabilizerLogicHost host) {
            for (ParticleStabilizerLogic logic : logics) {
                if (logic.tryTrigger(host)) {
                    logic.tick(host);
                } else {
                    logic.deactivate(host);
                }
            }
        }

        @Override
        public boolean handleCollision(ParticleStabilizerLogicHost host, FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event) {
            boolean success = false;
            for (ParticleStabilizerLogic logic : logics) {
                if (logic.handleCollision(host, entity, speed, event)) {
                    success = true;
                }
            }
            return success;
        }

        @Override
        public void deactivate(ParticleStabilizerLogicHost host) {
        }
    }
}

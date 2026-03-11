package icu.takeneko.nekoplus.block.tile.logic.stabilizer;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import icu.takeneko.nekoplus.all.NPAnvilMaterials;
import icu.takeneko.nekoplus.all.NPDataComponents;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.foundation.material.AnvilonType;
import icu.takeneko.nekoplus.foundation.recipes.SingleRecipeInput;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.util.ContainerUtil;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Objects;
import java.util.Optional;

public enum ParticleStabilizerLogics implements ParticleStabilizerLogic {
    ANVILON_COLLECTOR {
        @Override
        public boolean isValidTriggerItem(ItemStack stack) {
            return isValidEmptyContainerItem(stack);
        }

        @Override
        public boolean tryTrigger(ParticleStabilizerLogicHost host) {
            return isValidTriggerItem(host.getTriggerItem());
        }

        @Override
        public void tick(ParticleStabilizerLogicHost host) {
        }

        @Override
        public boolean handleCollision(ParticleStabilizerLogicHost host, FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event) {
            if (speed >= 32 && host.hasValidWorkingState() && isValidEmptyContainerItem(host.getTriggerItem())) {
                event.getLevel().playSound(
                    null,
                    host.getBlockPos(),
                    SoundEvents.ANVIL_LAND,
                    SoundSource.BLOCKS,
                    1.2f,
                    1.2f
                );
                entity.discard();
                processStabilize(host, entity.getBlockState().getBlock());
            } else {
                event.getLevel().playSound(
                    null,
                    host.getBlockPos(),
                    SoundEvents.ANVIL_DESTROY,
                    SoundSource.BLOCKS,
                    2f,
                    0.8f
                );
                event.getLevel().explode(
                    null,
                    host.getBlockPos().getX(),
                    host.getBlockPos().getY(),
                    host.getBlockPos().getZ(),
                    5,
                    Level.ExplosionInteraction.NONE
                );
                if (event.getLevel().getRandom().nextDouble() > 0.5) {
                    entity.discard();
                }
            }
            host.resetCooldown();
            host.resetState();
            event.setAnvilDamage(true);
            return true;
        }

        @Override
        public void deactivate(ParticleStabilizerLogicHost host) {

        }

        private void processStabilize(ParticleStabilizerLogicHost host, Block anvil) {
            if (isValidEmptyContainerItem(host.getTriggerItem())) {
                ItemStack itemStack = host.tryConsumeTriggerItem();
                if (itemStack.isEmpty()) return;
                itemStack = itemStack.copy();
                itemStack.set(NPDataComponents.CONTAINED_ANVILON_TYPE, AnvilonType.findType(anvil));
                itemStack.set(NPDataComponents.CONTAINED_ANVILION_STATUS.get(), AnvilonType.Contained.UNSTABLE);
                ItemStack retain = ContainerUtil.insertItem(host.getOutputItemHandler(), itemStack);
                if (retain.isEmpty()) return;
                Vec3 center = host.getBlockPos().getCenter();
                Containers.dropItemStack(host.getLevel(), center.x, center.y + 1, center.z, retain);
            }
        }
    },
    GAS_COLLECTOR {
        @Override
        public boolean isValidTriggerItem(ItemStack stack) {
            return stack.is(NPItems.AIR_FILTER);
        }

        @Override
        public boolean tryTrigger(ParticleStabilizerLogicHost host) {
            return isValidTriggerItem(host.getTriggerItem());
        }

        @Override
        public void tick(ParticleStabilizerLogicHost host) {
            Holder<DimensionType> holder = host.getLevel().dimensionTypeRegistration();
            AirCondensingRecipe currentRecipe = host.getCurrentRecipe();
            if (currentRecipe == null || Objects.equals(currentRecipe.getDimension().getKey(), holder.getKey())) {
                Optional<RecipeHolder<AirCondensingRecipe>> recipe = ServerLifecycleHooks.getCurrentServer().getRecipeManager()
                    .getRecipeFor(
                        NPRecipeTypes.AIR_CONDENSING,
                        SingleRecipeInput.of(holder.value()),
                        host.getLevel()
                    );
                if (recipe.isPresent()) {
                    host.setCurrentRecipe(recipe.get().value());
                    host.setMaxProgress(recipe.get().value().getTicks());
                    currentRecipe = recipe.get().value();
                } else {
                    host.setProgress(0);
                    host.setCurrentRecipe(null);
                    currentRecipe = null;
                }
            }
            if (currentRecipe != null) {
                int currentProgress = host.getProgress();
                if (currentProgress + 1 > currentRecipe.getTicks()) {
                    currentProgress = 0;
                    LootContext context = new LootContext.Builder(
                        new LootParams.Builder((ServerLevel) host.getLevel())
                            .create(LootContextParamSet.builder().build())
                    ).create(Optional.empty());
                    float v = currentRecipe.getProbability().getFloat(context);
                    if (host.getLevel().random.nextFloat() < v) {
                        for (ItemStack result : currentRecipe.getResults()) {
                            ContainerUtil.insertItem(host.getOutputItemHandler(), result.copy());
                        }
                    }
                } else {
                    currentProgress++;
                }
                host.setProgress(currentProgress);
            }
        }

        @Override
        public boolean handleCollision(ParticleStabilizerLogicHost host, FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event) {
            return false;
        }

        @Override
        public void deactivate(ParticleStabilizerLogicHost host) {
            host.setProgress(0);
            host.setCurrentRecipe(null);
        }
    };

    public static boolean isValidEmptyContainerItem(ItemStack stack) {
        return stack.is(NPItems.MAGNETIC_CONFINEMENT_VESSEL)
            && stack.getOrDefault(NPDataComponents.CONTAINED_ANVILON_TYPE.get(), NPAnvilMaterials.EMPTY) == NPAnvilMaterials.EMPTY;
    }
}

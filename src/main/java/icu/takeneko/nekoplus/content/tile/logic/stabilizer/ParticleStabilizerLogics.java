package icu.takeneko.nekoplus.content.tile.logic.stabilizer;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.foundation.recipes.SingleRecipeInput;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Objects;
import java.util.Optional;

public enum ParticleStabilizerLogics implements ParticleStabilizerLogic {
    GAS_COLLECTOR {
        @Override
        public boolean isValidTriggerItem(ItemResource stack) {
            return stack.is(NPItems.AIR_FILTER.asItem());
        }

        @Override
        public boolean tryTrigger(ParticleStabilizerLogicHost host) {
            return isValidTriggerItem(host.getTriggerResource());
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
                    host.setMaxProgress(1);
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
                            .create(new ContextKeySet.Builder().build())
                    ).create(Optional.empty());
                    float v = currentRecipe.getProbability().getFloat(context);
                    if (host.getLevel().getRandom().nextFloat() < v) {
                        for (ItemStack result : currentRecipe.getResultsAsItemStack()) {
                            ItemHandlerUtil.insertItem(host.getOutputItemHandler(), result.copy(), false);
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
}

package icu.takeneko.nekoplus.content.tile.logic.stabilizer;

import dev.anvilcraft.lib.v2.util.predicate.ChanceItemStack;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import dev.dubhe.anvilcraft.init.recipe.ModRecipeTypes;
import dev.dubhe.anvilcraft.recipe.anvil.collision.AnvilCollisionCraftRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.collision.BlockTransform;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.foundation.recipes.SingleRecipeInput;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
        public boolean handleCollision(
            ParticleStabilizerLogicHost host,
            FallingBlockEntity entity,
            double speed,
            AnvilEvent.CollisionBlock event
        ) {
            return false;
        }

        @Override
        public void deactivate(ParticleStabilizerLogicHost host) {
            host.setProgress(0);
            host.setCurrentRecipe(null);
        }
    },
    ANVIL_COLLISION_CRAFT {
        @Override
        public boolean isValidTriggerItem(ItemResource stack) {
            return !stack.isEmpty() && !blockStateOf(stack).isAir();
        }

        @Override
        public boolean tryTrigger(ParticleStabilizerLogicHost host) {
            return true;
        }

        @Override
        public void tick(ParticleStabilizerLogicHost host) {
        }

        @Override
        public boolean handleCollision(
            ParticleStabilizerLogicHost host,
            FallingBlockEntity entity,
            double speed,
            AnvilEvent.CollisionBlock event
        ) {
            if (!(host.getLevel() instanceof ServerLevel level)) return false;
            BlockState hitState = blockStateOf(host.getTriggerResource());
            if (hitState.isAir()) return false;
            RecipeHolder<AnvilCollisionCraftRecipe> matched = null;
            for (RecipeHolder<AnvilCollisionCraftRecipe> holder : level.getServer().getRecipeManager()
                .recipeMap()
                .byType(ModRecipeTypes.ANVIL_COLLISION_CRAFT.get())) {
                AnvilCollisionCraftRecipe recipe = holder.value();
                if (!recipe.anvil().test(level, entity.getBlockState(), null)) continue;
                if (!recipe.hitBlock().test(level, hitState, null)) continue;
                if (speed < recipe.speed()) continue;
                if (matched != null && matched.value().speed() > recipe.speed()) continue;
                matched = holder;
            }
            if (matched == null) return false;
            AnvilCollisionCraftRecipe recipe = matched.value();
            if (recipe.consume()) {
                entity.discard();
            }
            processTransforms(host, level, recipe);
            for (ChanceItemStack outputItem : recipe.outputItems()) {
                ItemStack itemStack = outputItem.getResult(level).create();
                if (!itemStack.isEmpty()) {
                    ItemHandlerUtil.insertItem(host.getOutputItemHandler(), itemStack, false);
                }
            }
            host.setCooldown(ParticleStabilizerBlockEntity.MACHINE_COOLDOWN * recipe.speed() / 32);
            return true;
        }

        private void processTransforms(ParticleStabilizerLogicHost host, ServerLevel level, AnvilCollisionCraftRecipe recipe) {
            ItemResource secondInput = host.getSecondInputResource();
            if (secondInput.isEmpty()) return;
            BlockState secondState = blockStateOf(secondInput);
            for (BlockTransform transform : recipe.transformBlocks()) {
                if (!transform.inputBlock().test(level, secondState, null)) continue;
                int limit = Math.min(transform.maxCount(), host.getSecondInputCount());
                for (int i = 0; i < limit; i++) {
                    var result = transform.outputBlock().getResult(level);
                    if (result == null) continue;
                    ItemStack output = new ItemStack(result.getKey().getBlock().asItem());
                    if (output.isEmpty()) continue;
                    if (!ItemHandlerUtil.insertItem(host.getOutputItemHandler(), output.copy(), true).isEmpty()) break;
                    ItemHandlerUtil.insertItem(host.getOutputItemHandler(), output, false);
                    host.tryConsumeSecondInput(1);
                }
            }
        }

        @Override
        public void deactivate(ParticleStabilizerLogicHost host) {
        }
    };

    private static BlockState blockStateOf(ItemResource resource) {
        if (resource.isEmpty()) return Blocks.AIR.defaultBlockState();
        return Block.byItem(resource.toStack(1).getItem()).defaultBlockState();
    }
}

package icu.takeneko.highenergyanvilology.all;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.anvilcraft.lib.AnvilLib;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.block.StampingPlatformBlock;
import dev.dubhe.anvilcraft.event.FallingBlockCollisionEventListener;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.highenergyanvilology.client.renderer.bewlr.MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.block.tile.BlockCollisionEventReceiver;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilMaterial;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilonType;
import icu.takeneko.highenergyanvilology.internal.StampingPlatformsInternals;
import icu.takeneko.highenergyanvilology.recipe.LaserEtchingRecipe;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ArrayListDeque;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@EventBusSubscriber
public class HEEvents {
    @SubscribeEvent
    public static void on(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            HEBlockEntities.ANVILION_EMITTER.get(),
            (a, v) -> a.getItemHandler()
        );
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            HEBlockEntities.PARTICLE_STABILIZER.get(),
            (a, v) -> {
                if (v == null) return a.getItemHandler();
                if (v == Direction.DOWN) return a.getItemHandler().slice(1, 5, true);
                return a.getItemHandler().slice(0, 1);
            }
        );

        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            HEBlockEntities.ITEM_INPUT_HATCH.get(),
            HatchLogic::getCapability
        );

        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            HEBlockEntities.ITEM_OUTPUT_HATCH.get(),
            HatchLogic::getCapability
        );

        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HEBlockEntities.ENERGY_OUTPUT_HATCH.get(),
            HatchLogic::getCapability
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void on(AnvilEvent.CollisionBlock event) {
        Level level = event.getLevel();
        if (level instanceof ClientLevel) return;
        BlockPos pos = event.getPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof BlockCollisionEventReceiver receiver) {
            if (receiver.acceptCollision(event.getEntity(), event.getSpeed(), event)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void on(AnvilEvent.OnLand event) {
        BlockPos eventPos = event.getPos();
        Level level = event.getLevel();
        if (!level.getBlockState(eventPos.below()).is(Blocks.CAULDRON)) return;
        AABB box = new AABB(eventPos.below());
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, box);
        Deque<Pair<ItemStack, ItemEntity>> powders = new ArrayListDeque<>();
        Map<ItemStack, ItemEntity> containersMap = new HashMap<>();
        for (ItemEntity entity : entities) {
            ItemStack entityItem = entity.getItem();
            if (entityItem.is(HEItems.STABILIZE_POWDER)) {
                powders.push(Pair.of(entityItem.copy(), entity));
                continue;
            }

            if (entityItem.is(HEItems.MAGNETIC_CONFINEMENT_VESSEL)) {
                AnvilMaterial material = entityItem.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE, HEAnvilMaterials.EMPTY);
                AnvilonType.Contained status = entityItem.getOrDefault(HEDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE);
                if (status != AnvilonType.Contained.UNSTABLE) continue;
                if (material == HEAnvilMaterials.EMPTY) continue;
                containersMap.put(entityItem, entity);
            }
        }
        Vec3 position = null;
        for (Map.Entry<ItemStack, ItemEntity> entry : containersMap.entrySet()) {
            ItemStack stack = entry.getKey();
            ItemEntity entity = entry.getValue();
            Pair<ItemStack, ItemEntity> pair = powders.peek();
            while (pair != null && pair.left().isEmpty()) {
                powders.pop();
                if (pair.left().isEmpty()) {
                    pair.right().discard();
                }
                pair = powders.peek();
            }
            if (pair == null) break;
            pair.left().shrink(1);
            pair.right().setItem(pair.left());
            if (pair.left().isEmpty()) {
                pair.right().discard();
            }

            entity.discard();
            stack = stack.copy();
            stack.set(HEDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.STABLE);
            ItemEntity newEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), stack);
            position = entity.position();
            level.addFreshEntity(newEntity);
        }

        if (position != null) {
            level.explode(null, null, new FallingBlockCollisionEventListener.ItemImmuneExplosionDamage(), position.x, position.y, position.z, 1, false, Level.ExplosionInteraction.NONE);
        }
    }

    @SubscribeEvent
    public static void onLandOnStampingPlatform(AnvilEvent.OnLand event) {
        BlockPos eventPos = event.getPos();
        Level level = event.getLevel();
        BlockState blockState = level.getBlockState(eventPos.below());
        if (!blockState.is(ModBlocks.STAMPING_PLATFORM)) return;
        if (!blockState.getValue(StampingPlatformsInternals.LASER_TARGETED)) return;
        Direction facing = blockState.getValue(StampingPlatformBlock.FACING);
        Vec3 motion = new Vec3(facing.getStepX(), -0.4, facing.getStepZ()).scale(0.2);
        Vec3 position = eventPos.below().getBottomCenter().add(facing.getStepX() * 0.5 + 0.25 * facing.getStepX(), 0.55, facing.getStepZ() * 0.5 + 0.25 * facing.getStepX());
        AABB box = new AABB(eventPos.below());
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, box);
        int anvilEfficency = AnvilLib.CONFIG.inWorldRecipeMaxEfficiency;
        for (ItemEntity entity : entities) {
            if (anvilEfficency <= 0) return;
            ItemStack itemStack = entity.getItem();
            boolean shouldStopCrafting = false;
            int count = itemStack.getCount();
            int actualCount;
            if (count > anvilEfficency) {
                actualCount = anvilEfficency;
                ItemEntity unprocessed = new ItemEntity(
                    entity.level(),
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    entity.getItem().copyWithCount(count - anvilEfficency)
                );
                level.addFreshEntity(unprocessed);
                shouldStopCrafting = true;
            } else {
                anvilEfficency -= count;
                actualCount = count;
            }
            SingleRecipeInput input = new SingleRecipeInput(itemStack.copyWithCount(actualCount));
            ServerLevel serverLevel = (ServerLevel) level;
            Optional<RecipeHolder<LaserEtchingRecipe>> recipeHolderOptional = serverLevel.getRecipeManager().getRecipeFor(HERecipeTypes.LASER_ETCHING, input, serverLevel);
            if (recipeHolderOptional.isPresent() && actualCount != 0) {
                entity.discard();
                LaserEtchingRecipe recipe = recipeHolderOptional.get().value();
                ItemStack result = recipe.output().getResult(serverLevel);
                result = result.copyWithCount(result.getCount() * actualCount);
                int maxStackSize = result.getMaxStackSize();
                while (result.getCount() > maxStackSize) {
                    result.setCount(result.getCount() - maxStackSize);
                    ItemEntity resultEntity = new ItemEntity(
                        entity.level(),
                        0,
                        0,
                        0,
                        result.copyWithCount(maxStackSize)
                    );
                    resultEntity.setDeltaMovement(motion);
                    resultEntity.setPos(position);
                    serverLevel.addFreshEntity(resultEntity);
                }
                if (!result.isEmpty()) {
                    ItemEntity resultEntity = new ItemEntity(
                        entity.level(),
                        0,
                        0,
                        0,
                        result
                    );
                    resultEntity.setDeltaMovement(motion);
                    resultEntity.setPos(position);
                    serverLevel.addFreshEntity(resultEntity);
                }
            }
            if (shouldStopCrafting) {
                break;
            }
        }

    }

    @SubscribeEvent
    public static void on(FMLCommonSetupEvent event) {
        event.enqueueWork(HEItemTooltips::setupTooltips);
        event.enqueueWork(AnvilonType::handleRegistration);
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void on(RegisterMenuScreensEvent event) {
        }

        @SubscribeEvent
        public static void on(ClientTickEvent.Pre event) {
            Minecraft mc = Minecraft.getInstance();
            if (!(mc.screen instanceof ModularUIGuiContainer screen)) return;
            WidgetGroup mainGroup = screen.modularUI.mainGroup;
            if (mainGroup instanceof Tickable tickable) {
                tickable.tick();
            }
        }

        @SubscribeEvent
        public static void on(ModelEvent.RegisterAdditional event) {
            event.register(MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer.CONTAINER_MODEL_LOCATION);
            event.register(MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer.CONTENT_MODEL_LOCATION);
            event.register(MageneticConfinementVesselItemBlockEntityWithoutLevelRenderer.MAGNETIC_MODEL_LOCATION);
        }

        @SubscribeEvent
        public static void on(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> {
                    if (tintIndex == 0) {
                        return FastColor.ARGB32.opaque(stack.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE.get(), HEAnvilMaterials.EMPTY).color());
                    }
                    return -1;
                },
                HEItems.MAGNETIC_CONFINEMENT_VESSEL
            );
        }
    }
}

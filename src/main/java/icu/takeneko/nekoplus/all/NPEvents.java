package icu.takeneko.nekoplus.all;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import dev.anvilcraft.lib.v2.recipe.AnvilLibRecipe;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.block.workstation.StampingPlatformBlock;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.nekoplus.block.ShulkerHatchBlock;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.internal.StampingPlatformsInternals;
import icu.takeneko.nekoplus.recipe.LaserEtchingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
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
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public class NPEvents {
    @SubscribeEvent
    public static void on(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.Item.BLOCK,
            NPBlockEntities.PARTICLE_STABILIZER.get(),
            (a, v) -> {
                if (v == null) return a.getItemHandler();
                if (v == Direction.DOWN) return a.getItemHandler().slice(1, 5, true);
                return a.getItemHandler().slice(0, 1);
            }
        );

//        event.registerBlockEntity(
//            Capabilities.Item.BLOCK,
//            NPBlockEntities.ITEM_INPUT_HATCH.get(),
//            HatchLogic::getCapability
//        );
//
//        event.registerBlockEntity(
//            Capabilities.Item.BLOCK,
//            NPBlockEntities.ITEM_OUTPUT_HATCH.get(),
//            HatchLogic::getCapability
//        );
//
//        event.registerBlockEntity(
//            Capabilities.Energy.BLOCK,
//            NPBlockEntities.ENERGY_OUTPUT_HATCH.get(),
//            HatchLogic::getCapability
//        );
    }

    @SubscribeEvent
    public static void on(OnDatapackSyncEvent event) {
        event.sendRecipes(NPRecipeTypes.LASER_ETCHING);
        event.sendRecipes(NPRecipeTypes.AIR_CONDENSING);
    }

    @SubscribeEvent
    public static void on(PlayerInteractEvent.LeftClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Direction face = event.getFace();
        if (face == null) return;
        BlockState state = level.getBlockState(pos);
        if (state.is(NPBlocks.SHULKER_HATCH) && state.getValue(ShulkerHatchBlock.FACING) == face.getOpposite()) {
            System.out.println("event.getAction() = " + event.getAction());
            if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.START) {
                state.attack(level, pos, event.getEntity());
            }
            event.setCanceled(true);
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
        Vec3 position = eventPos.below().getBottomCenter().add(
            facing.getStepX() * 0.5 + 0.25 * facing.getStepX(),
            0.55,
            facing.getStepZ() * 0.5 + 0.25 * facing.getStepX()
        );
        AABB box = new AABB(eventPos.below());
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, box);
        int anvilEfficency = AnvilLibRecipe.CONFIG.inWorldRecipeMaxEfficiency;
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
            Optional<RecipeHolder<LaserEtchingRecipe>> recipeHolderOptional = level.getServer().getRecipeManager().getRecipeFor(
                NPRecipeTypes.LASER_ETCHING,
                input,
                serverLevel
            );
            if (recipeHolderOptional.isPresent() && actualCount != 0) {
                entity.discard();
                LaserEtchingRecipe recipe = recipeHolderOptional.get().value();
                ItemStack result = recipe.output().getResult(serverLevel).create();
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
        event.enqueueWork(NPItemTooltips::setupTooltips);
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void on(RegisterMenuScreensEvent event) {
        }

        @SubscribeEvent
        public static void on(ClientTickEvent.Pre event) {
            Minecraft mc = Minecraft.getInstance();
            if (!(mc.screen instanceof ModularUIScreen screen)) return;
            UIElement mainGroup = screen.modularUI.ui.rootElement;
            if (mainGroup instanceof Tickable tickable) {
                tickable.tick();
            }
        }

        @SubscribeEvent
        public static void on(ModelEvent.RegisterStandalone event) {
        }

        @SubscribeEvent
        public static void on(RegisterColorHandlersEvent.ItemTintSources event) {
        }
    }
}

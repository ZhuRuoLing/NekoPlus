package icu.takeneko.nekoplus.all;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import dev.anvilcraft.lib.v2.recipe.init.recipe.LibRecipeTypes;
import dev.dubhe.anvilcraft.api.event.LightningBoltStrikeEvent;
import icu.takeneko.nekoplus.block.HugeBatteryBlock;
import icu.takeneko.nekoplus.block.tile.BatteryBlockEntity;
import icu.takeneko.nekoplus.block.tile.BlastCrystalBlockEntity;
import icu.takeneko.nekoplus.config.NPConfig;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringLightningRodBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

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
    }

    @SubscribeEvent
    public static void on(OnDatapackSyncEvent event) {
        event.sendRecipes(LibRecipeTypes.IN_WORLD_RECIPE.get());
        event.sendRecipes(NPRecipeTypes.AIR_CONDENSING);
    }

    @SubscribeEvent
    public static void on(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        List<NPEnhancementModule> list = itemStack.get(NPDataComponents.ENHANCEMENT_MODULE);
        if (list == null || list.isEmpty()) return;
        for (NPEnhancementModule module : list) {
            module.applyAttributeModifier(event);
        }
    }

    @SubscribeEvent
    public static void on(PlayerInteractEvent.LeftClickBlock event) {
    }

    @SubscribeEvent
    public static void on(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!player.onGround() && event.getOriginalSpeed() < event.getOriginalSpeed() * 5) {
            ItemStack item = player.getItemBySlot(EquipmentSlot.CHEST);
            List<NPEnhancementModule> list = item.get(NPDataComponents.ENHANCEMENT_MODULE);
            if (list == null) return;
            for (NPEnhancementModule module : list) {
                if (module.getType() == NPEnhancementModules.ANTI_GRAVITY.get()) {
                    event.setNewSpeed(event.getNewSpeed() * 5);
                }
            }
        }
    }

    @SubscribeEvent
    public static void on(LightningBoltStrikeEvent event) {
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState blockState = level.getBlockState(pos);
        BlockEntity be = null;
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        if (belowState.is(NPBlocks.BATTERY)) {
            be = level.getBlockEntity(below);
        } else {
            if (belowState.is(NPBlocks.HUGE_BATTERY)) {
                be = level.getBlockEntity(((HugeBatteryBlock) belowState.getBlock()).getMainPartPos(below, belowState));
            }
        }
        if (blockState.getBlock() instanceof WeatheringLightningRodBlock block && be instanceof BatteryBlockEntity battery) {
            WeatheringCopper.WeatherState age = block.getAge();
            float multiplier = (float) (Math.pow(2, age.ordinal()) * 0.5);
            int charged = Mth.floor(NPConfig.BATTERY_CAPACITY.getAsLong() * multiplier);
            battery.chargeDirect(charged);
        }
    }

    @SubscribeEvent
    public static void on(FMLCommonSetupEvent event) {
        event.enqueueWork(NPItemTooltips::setupTooltips);
    }

    @SubscribeEvent
    public static void on(ModConfigEvent.Loading event) {
        reloadConfigAccess(event);
    }

    @SubscribeEvent
    public static void on(ModConfigEvent.Reloading event) {
        reloadConfigAccess(event);
    }

    private static void reloadConfigAccess(ModConfigEvent event) {
        if (event.getConfig().getSpec() == NPConfig.SPEC) {
            BlastCrystalBlockEntity.config().reload();
        }
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

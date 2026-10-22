package icu.takeneko.nekoplus.foundation.item.module.impl;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import icu.takeneko.nekoplus.foundation.item.module.type.GenericEnhancementModuleType;
import icu.takeneko.nekoplus.foundation.item.module.type.NPEnhancementModuleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.List;

public class PortableBatteryModule implements NPEnhancementModule {
    public static final PortableBatteryModule INSTANCE = new PortableBatteryModule();

    public static final Identifier NAME = NekoPlus.location("portable_battery");

    public static final MapCodec<PortableBatteryModule> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, PortableBatteryModule> STREAM_CODEC = StreamCodec.unit(
        INSTANCE
    );

    public static final GenericEnhancementModuleType<PortableBatteryModule> TYPE = NPEnhancementModuleType.generic(
        NAME,
        MAP_CODEC,
        STREAM_CODEC,
        () -> INSTANCE,
        NPItems.PORTABLE_BATTERY_MODULE,
        EquipmentSlotGroup.CHEST
    );

    @Override
    public void inventoryTick(Player player, ItemStack itemStack) {
    }

    @Override
    public void applyAttributeModifier(ItemAttributeModifierEvent event) {
    }

    @Override
    public Component name() {
        return Component.translatable("item.nekoplus.portable_battery_module");
    }

    @Override
    public List<Component> tooltip() {
        return List.of(
            Component.translatable("tooltip.nekoplus.portable_battery_module")
        );
    }

    @Override
    public NPEnhancementModuleType<?> getType() {
        return TYPE;
    }
}

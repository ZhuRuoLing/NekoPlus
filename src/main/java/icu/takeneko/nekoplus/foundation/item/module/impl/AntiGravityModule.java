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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.List;

public class AntiGravityModule implements NPEnhancementModule {
    public static final AntiGravityModule INSTANCE = new AntiGravityModule();
    public static final Identifier NAME = NekoPlus.location("anti_gravity");

    public static final MapCodec<AntiGravityModule> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, AntiGravityModule> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static final GenericEnhancementModuleType<AntiGravityModule> TYPE = NPEnhancementModuleType.generic(
        NAME,
        MAP_CODEC,
        STREAM_CODEC,
        () -> INSTANCE,
        NPItems.ANTI_GRAVITY_MODULE,
        EquipmentSlotGroup.CHEST
    );

    @Override
    public void inventoryTick(Player player, ItemStack itemStack) {
    }

    @Override
    public void applyAttributeModifier(ItemAttributeModifierEvent event) {
        event.addModifier(
            NeoForgeMod.CREATIVE_FLIGHT,
            new AttributeModifier(NAME, 1, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.CHEST
        );
    }

    @Override
    public Component name() {
        return Component.literal("tooltip.nekoplus.enhancement_module.anti_gravity.name");
    }

    @Override
    public List<Component> tooltip() {
        return List.of(
            Component.literal("tooltip.nekoplus.enhancement_module.anti_gravity.tooltip1")
        );
    }

    @Override
    public NPEnhancementModuleType<?> getType() {
        return TYPE;
    }
}

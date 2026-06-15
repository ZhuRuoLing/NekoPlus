package icu.takeneko.nekoplus.foundation.item.module.impl;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPDataComponents;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.List;

public class TitaniumCrystalModule implements NPEnhancementModule {
    public static final TitaniumCrystalModule INSTANCE = new TitaniumCrystalModule();

    public static final Identifier NAME = NekoPlus.location("titanium_crystal");

    public static final MapCodec<TitaniumCrystalModule> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, TitaniumCrystalModule> STREAM_CODEC = StreamCodec.unit(
        INSTANCE);

    public static final GenericEnhancementModuleType<TitaniumCrystalModule> TYPE = NPEnhancementModuleType.generic(
        NAME,
        MAP_CODEC,
        STREAM_CODEC,
        () -> INSTANCE,
        NPItems.TITANIUM_CRYSTAL_MODULE,
        EquipmentSlotGroup.HAND,
        4
    );

    @Override
    public void inventoryTick(Player player, ItemStack itemStack) {
    }

    @Override
    public void applyAttributeModifier(ItemAttributeModifierEvent event) {
        List<NPEnhancementModule> moduleList = event.getItemStack().get(NPDataComponents.ENHANCEMENT_MODULE);
        float base = 1f;
        for (NPEnhancementModule module : moduleList) {
            if (module instanceof TitaniumCrystalModule) {
                base *= 1.3f;
            }
        }
        event.addModifier(
            Attributes.ATTACK_DAMAGE,
            new AttributeModifier(NAME, base, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
            EquipmentSlotGroup.HAND
        );
    }

    @Override
    public Component name() {
        return Component.translatable("item.nekoplus.titanium_crystal_module");
    }

    @Override
    public List<Component> tooltip() {
        return List.of(
            Component.translatable("tooltip.nekoplus.titanium_crystal_module.desc")
        );
    }

    @Override
    public NPEnhancementModuleType<?> getType() {
        return TYPE;
    }
}

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

public class ExoskeletalLegFrameModule implements NPEnhancementModule {
    public static final ExoskeletalLegFrameModule INSTANCE = new ExoskeletalLegFrameModule();

    public static final Identifier NAME = NekoPlus.location("exoskeletal_leg_frame");
    public static final Identifier SPEED_MODIFIER = NekoPlus.location("exoskeletal_leg_frame_speed");
    public static final Identifier STEP_HEIGHT_MODIFIER = NekoPlus.location("exoskeletal_leg_frame_step_height");
    public static final Identifier JUMP_STRENGTH_MODIFIER = NekoPlus.location("exoskeletal_leg_frame_jump_boost");

    public static final MapCodec<ExoskeletalLegFrameModule> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ExoskeletalLegFrameModule> STREAM_CODEC = StreamCodec.unit(
        INSTANCE
    );

    public static final GenericEnhancementModuleType<ExoskeletalLegFrameModule> TYPE = NPEnhancementModuleType.generic(
        NAME,
        MAP_CODEC,
        STREAM_CODEC,
        () -> INSTANCE,
        NPItems.EXOSKELETAL_LEG_FRAME_MODULE,
        EquipmentSlotGroup.LEGS,
        3
    );

    @Override
    public void inventoryTick(Player player, ItemStack itemStack) {
    }

    @Override
    public void applyAttributeModifier(ItemAttributeModifierEvent event) {
        List<NPEnhancementModule> moduleList = event.getItemStack().get(NPDataComponents.ENHANCEMENT_MODULE);
        float speedMultiplier = 1;
        for (NPEnhancementModule module : moduleList) {
            if (module instanceof ExoskeletalLegFrameModule) {
                speedMultiplier *= 1.1f;
            }
        }

        event.addModifier(
            Attributes.MOVEMENT_SPEED,
            new AttributeModifier(
                SPEED_MODIFIER,
                speedMultiplier,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            EquipmentSlotGroup.LEGS
        );
        event.addModifier(
            Attributes.STEP_HEIGHT,
            new AttributeModifier(STEP_HEIGHT_MODIFIER, 0.5, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.LEGS
        );
        event.addModifier(
            Attributes.JUMP_STRENGTH,
            new AttributeModifier(JUMP_STRENGTH_MODIFIER, 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            EquipmentSlotGroup.LEGS
        );
    }

    @Override
    public Component name() {
        return Component.translatable("item.nekoplus.exoskeletal_leg_frame_module");
    }

    @Override
    public List<Component> tooltip() {
        return List.of(
            Component.translatable("tooltip.nekoplus.exoskeletal_leg_frame_module.desc")
        );
    }

    @Override
    public NPEnhancementModuleType<?> getType() {
        return TYPE;
    }
}

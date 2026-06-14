package icu.takeneko.nekoplus.foundation.item.module;

import com.mojang.serialization.Codec;
import icu.takeneko.nekoplus.foundation.item.module.type.NPEnhancementModuleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.List;

public interface NPEnhancementModule {

    Codec<NPEnhancementModule> CODEC = NPEnhancementModuleType.CODEC.dispatch(
        NPEnhancementModule::getType,
        NPEnhancementModuleType::codec
    );

    Codec<List<NPEnhancementModule>> LIST_CODEC = CODEC.listOf();

    StreamCodec<RegistryFriendlyByteBuf, NPEnhancementModule> STREAM_CODEC = NPEnhancementModuleType.STREAM_CODEC
        .dispatch(
            NPEnhancementModule::getType,
            NPEnhancementModuleType::streamCodec
        );

    StreamCodec<RegistryFriendlyByteBuf, List<NPEnhancementModule>> LIST_STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, NPEnhancementModule>list()
        .apply(NPEnhancementModule.STREAM_CODEC);

    void inventoryTick(Player player, ItemStack itemStack);

    void applyAttributeModifier(ItemAttributeModifierEvent event);

    Component name();

    List<Component> tooltip();

    NPEnhancementModuleType<?> getType();

    interface Factory<T extends NPEnhancementModule> {
        T create();
    }
}

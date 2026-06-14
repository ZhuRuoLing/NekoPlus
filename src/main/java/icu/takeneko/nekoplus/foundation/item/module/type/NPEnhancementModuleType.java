package icu.takeneko.nekoplus.foundation.item.module.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.all.NPRegistries;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface NPEnhancementModuleType<T extends NPEnhancementModule> {
    Codec<NPEnhancementModuleType<?>> CODEC = Codec.lazyInitialized(() -> NPRegistries.getEnhancementModuleTypeRegistry().byNameCodec());

    StreamCodec<RegistryFriendlyByteBuf, NPEnhancementModuleType<?>> STREAM_CODEC = ByteBufCodecs.registry(
        NPRegistries.ENHANCEMENT_MODULE_TYPE_KEY
    );

    Identifier name();

    MapCodec<T> codec();

    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

    NPEnhancementModule.Factory<T> factory();

    boolean appliesTo(ItemStack itemStack);

    void postAssemble(ItemStack itemStack);

    Holder<Item> itemHolder();

    int installationLimit();

    static <T1 extends NPEnhancementModule> GenericEnhancementModuleType<T1> generic(
        Identifier name,
        MapCodec<T1> codec,
        StreamCodec<RegistryFriendlyByteBuf, T1> streamCodec,
        NPEnhancementModule.Factory<T1> factory,
        Holder<Item> itemHolder,
        EquipmentSlotGroup slotGroup
    ){
        return new GenericEnhancementModuleType<>(
            name,
            codec,
            streamCodec,
            factory,
            itemHolder,
            slotGroup
        );
    }
}

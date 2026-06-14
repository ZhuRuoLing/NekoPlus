package icu.takeneko.nekoplus.foundation.item.module.type;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record SimpleUniversalEnhancementModuleType<T extends NPEnhancementModule>(
    Identifier name,
    MapCodec<T> codec,
    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec,
    NPEnhancementModule.Factory<T> factory,
    Holder<Item> itemHolder
) implements NPEnhancementModuleType<T> {
    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return true;
    }
}

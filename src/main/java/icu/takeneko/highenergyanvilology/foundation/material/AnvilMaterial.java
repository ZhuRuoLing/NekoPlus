package icu.takeneko.highenergyanvilology.foundation.material;

import com.mojang.serialization.Codec;
import icu.takeneko.highenergyanvilology.all.HEBuiltinRegistries;
import icu.takeneko.highenergyanvilology.all.HERegistries;
import lombok.Builder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

@Builder
public record AnvilMaterial(
    float processRate,
    int maxDamage,
    int color,
//     Supplier<Item> materialBlockItemRef,
//     Supplier<Item> materialIngotItemRef,
    Supplier<Item> anvilItem,
    Supplier<Block> anvilBlock
) {
    public static final Codec<AnvilMaterial> CODEC = HEBuiltinRegistries.MATERIAL.byNameCodec();
    public static final StreamCodec<? super RegistryFriendlyByteBuf, AnvilMaterial> STREAM_CODEC = ByteBufCodecs.registry(HERegistries.MATERIAL);
}

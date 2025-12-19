package icu.takeneko.highenergyanvilology.all;

import com.mojang.serialization.MapCodec;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.foundation.recipes.RecipeSerializerImpl;
import icu.takeneko.highenergyanvilology.recipes.AirCondensingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HERecipeTypes {
    public static final DeferredRegister<RecipeType<?>> DR = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, HEAnvilology.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DR = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, HEAnvilology.MODID);

    public static final RecipeType<AirCondensingRecipe> AIR_CONDENSING = registerType("air_condensing");
    public static final RecipeSerializer<AirCondensingRecipe> AIR_CONDENSING_SERIALIZER = registerSerializer(
        "air_condensing",
        AirCondensingRecipe.MAP_CODEC,
        AirCondensingRecipe.STREAM_CODEC
    );

    private static <T extends Recipe<?>> RecipeType<T> registerType(String name) {
        RecipeType<T> type = RecipeType.simple(HEAnvilology.location(name));
        DR.register(name, () -> type);
        return type;
    }

    private static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        RecipeSerializer<T> serializer = new RecipeSerializerImpl(codec, streamCodec);
        RECIPE_SERIALIZER_DR.register(name, () -> serializer);
        return serializer;
    }
}

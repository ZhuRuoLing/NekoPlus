package icu.takeneko.nekoplus.all;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.recipe.LaserEtchingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NPRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> DR = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, NekoPlus.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DR = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, NekoPlus.MODID);

    public static final RecipeType<AirCondensingRecipe> AIR_CONDENSING = registerType("air_condensing");
    public static final RecipeSerializer<AirCondensingRecipe> AIR_CONDENSING_SERIALIZER = registerSerializer(
        "air_condensing",
        AirCondensingRecipe.MAP_CODEC,
        AirCondensingRecipe.STREAM_CODEC
    );

    public static final RecipeType<LaserEtchingRecipe> LASER_ETCHING = registerType("laser_etching");
    public static final RecipeSerializer<LaserEtchingRecipe> LASER_ETCHING_SERIALIZER = registerSerializer(
        "laser_etching",
        LaserEtchingRecipe.MAP_CODEC,
        LaserEtchingRecipe.STREAM_CODEC
    );

    private static <T extends Recipe<?>> RecipeType<T> registerType(String name) {
        RecipeType<T> type = RecipeType.simple(NekoPlus.location(name));
        DR.register(name, () -> type);
        return type;
    }

    private static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        RecipeSerializer<T> serializer = new RecipeSerializer<>(codec, streamCodec);
        RECIPE_SERIALIZER_DR.register(name, () -> serializer);
        return serializer;
    }
}

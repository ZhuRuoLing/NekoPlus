package icu.takeneko.highenergyanvilology.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import icu.takeneko.highenergyanvilology.all.HERecipeTypes;
import icu.takeneko.highenergyanvilology.foundation.recipes.SingleRecipeInput;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AirCondensingRecipe implements Recipe<SingleRecipeInput<DimensionType>> {
    public static final MapCodec<AirCondensingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(ins ->
        ins.group(
            DimensionType.CODEC.fieldOf("dimension").forGetter(AirCondensingRecipe::getDimension),
            ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(AirCondensingRecipe::getResults)
        ).apply(ins, AirCondensingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AirCondensingRecipe> STREAM_CODEC = StreamCodec.composite(
        DimensionType.STREAM_CODEC,
        AirCondensingRecipe::getDimension,
        ByteBufCodecs.collection(ArrayList::new, ItemStack.STREAM_CODEC),
        AirCondensingRecipe::getResults,
        AirCondensingRecipe::new
    );


    private final Holder<DimensionType> dimension;
    private final List<ItemStack> results;

    public AirCondensingRecipe(
        Holder<DimensionType> dimension,
        List<ItemStack> results
    ) {
        this.dimension = dimension;
        this.results = results;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AirCondensingRecipe) obj;
        return Objects.equals(this.dimension, that.dimension) &&
            Objects.equals(this.results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, results);
    }

    @Override
    public String toString() {
        return "AirCondensingRecipe[" +
            "dimension=" + dimension + ", " +
            "results=" + results + ']';
    }

    @Override
    public boolean matches(SingleRecipeInput<DimensionType> input, Level level) {
        return input.unwrap() == dimension.value();
    }

    @Override
    public ItemStack assemble(SingleRecipeInput<DimensionType> input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HERecipeTypes.AIR_CONDENSING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return HERecipeTypes.AIR_CONDENSING;
    }
}

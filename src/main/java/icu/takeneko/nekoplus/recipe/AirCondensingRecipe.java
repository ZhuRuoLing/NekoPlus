package icu.takeneko.nekoplus.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumRecipeProvider;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.foundation.recipes.SingleRecipeInput;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AirCondensingRecipe implements Recipe<SingleRecipeInput<DimensionType>> {
    public static final MapCodec<AirCondensingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(ins ->
        ins.group(
            DimensionType.CODEC.fieldOf("dimension").forGetter(AirCondensingRecipe::getDimension),
            ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(AirCondensingRecipe::getResults),
            NumberProviders.CODEC.fieldOf("probability").forGetter(AirCondensingRecipe::getProbability),
            Codec.INT.fieldOf("ticks").forGetter(AirCondensingRecipe::getTicks)
        ).apply(ins, AirCondensingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AirCondensingRecipe> STREAM_CODEC = StreamCodec.composite(
        DimensionType.STREAM_CODEC,
        AirCondensingRecipe::getDimension,
        ByteBufCodecs.collection(ArrayList::new, ItemStack.STREAM_CODEC),
        AirCondensingRecipe::getResults,
        ByteBufCodecs.fromCodec(NumberProviders.CODEC),
        AirCondensingRecipe::getProbability,
        ByteBufCodecs.INT,
        AirCondensingRecipe::getTicks,
        AirCondensingRecipe::new
    );


    private final Holder<DimensionType> dimension;
    private final List<ItemStack> results;
    private final NumberProvider probability;
    private final int ticks;

    public AirCondensingRecipe(
        Holder<DimensionType> dimension,
        List<ItemStack> results,
        NumberProvider probability,
        int ticks
    ) {
        this.dimension = dimension;
        this.results = results;
        this.probability = probability;
        this.ticks = ticks;
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
        return NPRecipeTypes.AIR_CONDENSING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return NPRecipeTypes.AIR_CONDENSING;
    }

    public void save(Identifier id, RegistrumRecipeProvider output) {
        Advancement.Builder builder = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR);

        output.accept(id, this, builder.build(id.withPrefix("recipes/air_condensing/")));
    }
}

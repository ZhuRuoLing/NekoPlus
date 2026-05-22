package icu.takeneko.nekoplus.recipe;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.v2.registrum.providers.generators.RegistrumRecipeProvider;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.foundation.recipes.SingleRecipeInput;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
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
            ItemStackTemplate.CODEC.listOf().fieldOf("results").forGetter(AirCondensingRecipe::getResults),
            NumberProviders.CODEC.fieldOf("probability").forGetter(AirCondensingRecipe::getProbability),
            Codec.INT.fieldOf("ticks").forGetter(AirCondensingRecipe::getTicks)
        ).apply(ins, AirCondensingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AirCondensingRecipe> STREAM_CODEC = StreamCodec.composite(
        DimensionType.STREAM_CODEC,
        AirCondensingRecipe::getDimension,
        ByteBufCodecs.collection(ArrayList::new, ItemStackTemplate.STREAM_CODEC),
        AirCondensingRecipe::getResults,
        ByteBufCodecs.fromCodec(NumberProviders.CODEC),
        AirCondensingRecipe::getProbability,
        ByteBufCodecs.INT,
        AirCondensingRecipe::getTicks,
        AirCondensingRecipe::new
    );

    private final Holder<DimensionType> dimension;
    private final List<ItemStackTemplate> results;
    private final NumberProvider probability;
    private final int ticks;

    public AirCondensingRecipe(
        Holder<DimensionType> dimension,
        List<ItemStackTemplate> results,
        NumberProvider probability,
        int ticks
    ) {
        this.dimension = dimension;
        this.results = results;
        this.probability = probability;
        this.ticks = ticks;
    }

    public List<ItemStack> getResultsAsItemStack() {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStackTemplate it : results) {
            ItemStack itemStack = it.create();
            list.add(itemStack);
        }
        return list;
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
    public ItemStack assemble(SingleRecipeInput<DimensionType> dimensionTypeSingleRecipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "air_condensing";
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput<DimensionType>>> getSerializer() {
        return NPRecipeTypes.AIR_CONDENSING_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput<DimensionType>>> getType() {
        return NPRecipeTypes.AIR_CONDENSING;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public void save(RegistrumRecipeProvider output, Identifier id) {
        ResourceKey<Recipe<?>> resourceKey = ResourceKey.create(Registries.RECIPE, id);
        Advancement.Builder builder = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
            .rewards(AdvancementRewards.Builder.recipe(resourceKey))
            .requirements(AdvancementRequirements.Strategy.OR);

        output.accept(resourceKey, this, builder.build(id.withPrefix("recipes/air_condensing/")));
    }
}

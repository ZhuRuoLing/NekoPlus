package icu.takeneko.nekoplus.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.v2.registrum.providers.generators.RegistrumRecipeProvider;
import dev.anvilcraft.lib.v2.util.predicate.ChanceItemStack;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import lombok.Builder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

@Builder
public record LaserEtchingRecipe(Ingredient input, ChanceItemStack output) implements Recipe<SingleRecipeInput> {

    public static final MapCodec<LaserEtchingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(ins ->
        ins.group(
            Ingredient.CODEC.fieldOf("input").forGetter(LaserEtchingRecipe::input),
            ChanceItemStack.CODEC.fieldOf("output").forGetter(LaserEtchingRecipe::output)
        ).apply(ins, LaserEtchingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LaserEtchingRecipe> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC,
        LaserEtchingRecipe::input,
        ChanceItemStack.STREAM_CODEC,
        LaserEtchingRecipe::output,
        LaserEtchingRecipe::new
    );

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.input.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput singleRecipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "laser_etching";
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return NPRecipeTypes.LASER_ETCHING_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return NPRecipeTypes.LASER_ETCHING;
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

        output.accept(resourceKey, this, builder.build(id.withPrefix("recipes/laser_etching/")));
    }
}

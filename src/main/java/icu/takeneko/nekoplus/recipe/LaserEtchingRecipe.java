package icu.takeneko.nekoplus.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.lib.recipe.component.ChanceItemStack;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import lombok.Builder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
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
        return NPRecipeTypes.LASER_ETCHING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return NPRecipeTypes.LASER_ETCHING;
    }

    public void save(ResourceLocation id, RegistrateRecipeProvider output) {
        Advancement.Builder builder = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR);

        output.accept(id, this, builder.build(id.withPrefix("recipes/laser_etching/")));
    }
}

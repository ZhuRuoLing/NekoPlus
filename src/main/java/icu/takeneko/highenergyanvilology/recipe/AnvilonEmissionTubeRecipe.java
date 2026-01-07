package icu.takeneko.highenergyanvilology.recipe;

import icu.takeneko.highenergyanvilology.all.HERecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AnvilonEmissionTubeRecipe extends CustomRecipe {
    public AnvilonEmissionTubeRecipe() {
        super(CraftingBookCategory.REDSTONE);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HERecipeTypes.ANVILON_EMISSION_TUBE_SERIALIZER;
    }
}

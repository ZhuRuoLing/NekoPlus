package icu.takeneko.highenergyanvilology.recipe;

import icu.takeneko.highenergyanvilology.all.HEAnvilMaterials;
import icu.takeneko.highenergyanvilology.all.HEDataComponents;
import icu.takeneko.highenergyanvilology.all.HEItems;
import icu.takeneko.highenergyanvilology.all.HERecipeTypes;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilMaterial;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilonType;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AnvilonEmissionTubeRecipe extends CustomRecipe {

    public static final AnvilonEmissionTubeRecipe INSTANCE = new AnvilonEmissionTubeRecipe();

    public AnvilonEmissionTubeRecipe() {
        super(CraftingBookCategory.MISC);
    }

    public AnvilonEmissionTubeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        boolean hasTube = false;
        boolean hasContainer = false;
        for (ItemStack item : craftingInput.items()) {
            if (item.isEmpty()) continue;
            if (item.is(HEItems.MAGNETIC_CONFINEMENT_VESSEL)
                && item.getOrDefault(HEDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE) == AnvilonType.Contained.STABLE
                && item.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE, HEAnvilMaterials.EMPTY) != HEAnvilMaterials.EMPTY
            ) {
                if (hasContainer) {
                    return false;
                }
                hasContainer = true;
            }
            if (item.is(HEItems.ANVILON_EMISSION_TUBE)
                && item.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE, HEAnvilMaterials.EMPTY) == HEAnvilMaterials.EMPTY
            ) {
                if (hasTube) {
                    return false;
                }
                hasTube = true;
            }
        }
        return hasContainer && hasTube;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        AnvilMaterial anvilMaterial = null;
        for (ItemStack item : craftingInput.items()) {
            if (item.is(HEItems.MAGNETIC_CONFINEMENT_VESSEL)
                && item.getOrDefault(HEDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE) == AnvilonType.Contained.STABLE
                && item.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE, HEAnvilMaterials.EMPTY) != HEAnvilMaterials.EMPTY
            ) {
                anvilMaterial = item.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE, HEAnvilMaterials.EMPTY);
            }
        }
        ItemStack result = HEItems.ANVILON_EMISSION_TUBE.asStack();
        result.set(HEDataComponents.CONTAINED_ANVILON_TYPE, anvilMaterial);
        return result;
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

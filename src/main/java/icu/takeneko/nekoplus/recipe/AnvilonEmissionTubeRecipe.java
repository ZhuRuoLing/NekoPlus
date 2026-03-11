package icu.takeneko.nekoplus.recipe;

import icu.takeneko.nekoplus.all.NPAnvilMaterials;
import icu.takeneko.nekoplus.all.NPDataComponents;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.foundation.material.AnvilMaterial;
import icu.takeneko.nekoplus.foundation.material.AnvilonType;
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
            if (item.is(NPItems.MAGNETIC_CONFINEMENT_VESSEL)
                && item.getOrDefault(NPDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE) == AnvilonType.Contained.STABLE
                && item.getOrDefault(NPDataComponents.CONTAINED_ANVILON_TYPE, NPAnvilMaterials.EMPTY) != NPAnvilMaterials.EMPTY
            ) {
                if (hasContainer) {
                    return false;
                }
                hasContainer = true;
            }
            if (item.is(NPItems.ANVILON_EMISSION_TUBE)
                && item.getOrDefault(NPDataComponents.CONTAINED_ANVILON_TYPE, NPAnvilMaterials.EMPTY) == NPAnvilMaterials.EMPTY
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
            if (item.is(NPItems.MAGNETIC_CONFINEMENT_VESSEL)
                && item.getOrDefault(NPDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE) == AnvilonType.Contained.STABLE
                && item.getOrDefault(NPDataComponents.CONTAINED_ANVILON_TYPE, NPAnvilMaterials.EMPTY) != NPAnvilMaterials.EMPTY
            ) {
                anvilMaterial = item.getOrDefault(NPDataComponents.CONTAINED_ANVILON_TYPE, NPAnvilMaterials.EMPTY);
            }
        }
        ItemStack result = NPItems.ANVILON_EMISSION_TUBE.asStack();
        result.set(NPDataComponents.CONTAINED_ANVILON_TYPE, anvilMaterial);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return NPRecipeTypes.ANVILON_EMISSION_TUBE_SERIALIZER;
    }
}

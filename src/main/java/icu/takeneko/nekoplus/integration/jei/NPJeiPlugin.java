package icu.takeneko.nekoplus.integration.jei;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class NPJeiPlugin implements IModPlugin {
    public static final RecipeType<RecipeHolder<AirCondensingRecipe>> AIR_CONDENSING_TYPE = createRecipeHolderType("air_condensing");
    public static final RecipeType<RecipeHolder<AirCondensingRecipe>> LASER_ETCHING_TYPE = createRecipeHolderType("laser_etching");

    @Override
    public ResourceLocation getPluginUid() {
        return NekoPlus.location("jei");
    }

    public static <R extends Recipe<?>> RecipeType<RecipeHolder<R>> createRecipeHolderType(String name) {
        return RecipeType.createRecipeHolderType(NekoPlus.location(name));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
            AIR_CONDENSING_TYPE,
            Minecraft.getInstance().getConnection().getRecipeManager().getAllRecipesFor(NPRecipeTypes.AIR_CONDENSING)
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(
            AIR_CONDENSING_TYPE,
            NPItems.AIR_FILTER,
            NPBlocks.PARTICLE_STABILIZER
        );
    }
}

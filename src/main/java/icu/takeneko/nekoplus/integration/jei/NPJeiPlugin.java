package icu.takeneko.nekoplus.integration.jei;

import dev.anvilcraft.lib.v2.recipe.InWorldRecipe;
import dev.anvilcraft.lib.v2.recipe.init.recipe.LibRecipeTypes;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.all.NPRecipeTypes;
import icu.takeneko.nekoplus.client.NekoPlusClient;
import icu.takeneko.nekoplus.integration.jei.categories.AirCondensingRecipeCategory;
import icu.takeneko.nekoplus.integration.jei.categories.LaserEtchingRecipeCategory;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import lombok.extern.slf4j.Slf4j;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@Slf4j
public class NPJeiPlugin implements IModPlugin {
    public static IRecipeType<RecipeHolder<AirCondensingRecipe>> AIR_CONDENSING_TYPE;
    public static IRecipeType<RecipeHolder<InWorldRecipe>> LASER_ETCHING_TYPE;

    @Override
    public Identifier getPluginUid() {
        return NekoPlus.location("jei");
    }

    public static <R extends Recipe<I>, I extends RecipeInput> IRecipeType<RecipeHolder<R>> createRecipeHolderType(
        RecipeType<R> type
    ) {
        return IRecipeType.create(type);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RecipeHolder<AirCondensingRecipe>> airCondensing = new ArrayList<>(NekoPlusClient.getSyncedRecipes().byType(NPRecipeTypes.AIR_CONDENSING));
        List<RecipeHolder<InWorldRecipe>> laserEtching = NekoPlusClient.getSyncedRecipes()
            .byType(LibRecipeTypes.IN_WORLD_RECIPE.get()).stream()
            .filter(holder -> NekoPlus.MODID.equals(holder.id().identifier().getNamespace())
                && holder.id().identifier().getPath().startsWith("laser_etching/"))
            .toList();

        log.info("Loading {} recipes for {}", airCondensing.size(), NPRecipeTypes.AIR_CONDENSING);
        log.info("Loading {} laser etching recipes", laserEtching.size());

        registration.addRecipes(
            AIR_CONDENSING_TYPE,
            airCondensing
        );
        registration.addRecipes(
            LASER_ETCHING_TYPE,
            laserEtching

        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerCategories(IRecipeCategoryRegistration registration) {
        AIR_CONDENSING_TYPE = createRecipeHolderType(NPRecipeTypes.AIR_CONDENSING);
        LASER_ETCHING_TYPE = (IRecipeType<RecipeHolder<InWorldRecipe>>) (IRecipeType<?>)
            IRecipeType.create(NekoPlus.location("laser_etching"), RecipeHolder.class);
        registration.addRecipeCategories(
            new AirCondensingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new LaserEtchingRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(
            AIR_CONDENSING_TYPE,
            NPItems.AIR_FILTER,
            NPBlocks.PARTICLE_STABILIZER
        );

        registration.addCraftingStation(
            LASER_ETCHING_TYPE,
            NPBlocks.HIGH_ENERGY_LASER,
            ModBlocks.RUBY_LASER
        );
    }
}

package icu.takeneko.nekoplus.integration.jei.categories;

import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.integration.jei.NPJeiPlugin;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

public class AirCondensingRecipeCategory implements IRecipeCategory<RecipeHolder<AirCondensingRecipe>> {
    public static final Component TITLE = Component.translatable("category.nekoplus.air_condensing");

    private final IDrawable icon;

    public AirCondensingRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableItemStack(NPBlocks.PARTICLE_STABILIZER.asStack());

    }

    @Override
    public RecipeType<RecipeHolder<AirCondensingRecipe>> getRecipeType() {
        return NPJeiPlugin.AIR_CONDENSING_TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<AirCondensingRecipe> recipe, IFocusGroup focuses) {

    }
}

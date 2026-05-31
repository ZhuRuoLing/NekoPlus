package icu.takeneko.nekoplus.integration.jei.categories;

import dev.dubhe.anvilcraft.client.support.RenderSupport;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.integration.jei.NPJeiPlugin;
import icu.takeneko.nekoplus.integration.jei.NPJeiSlotUtil;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

public class AirCondensingRecipeCategory implements IRecipeCategory<RecipeHolder<AirCondensingRecipe>> {
    public static final Component TITLE = Component.translatable("category.nekoplus.air_condensing");

    private final IDrawable icon;
    private final IDrawable slotDefault;
    private final IDrawable slotProbability;
    private final IDrawable arrowIn;
    private final IDrawable arrowOut;
    private final IDrawable arrowOutputFromBelow;

    public AirCondensingRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableItemStack(NPBlocks.PARTICLE_STABILIZER.asStack());
        this.slotDefault = JeiRenderHelper.getSlotDefault(helper);
        this.slotProbability = JeiRenderHelper.getSlotProbability(helper);
        this.arrowIn = JeiRenderHelper.getArrowInput(helper);
        this.arrowOut = JeiRenderHelper.getArrowOutput(helper);
        this.arrowOutputFromBelow = JeiRenderHelper.getArrowOutputFromBelow(helper);
    }

    @Override
    public void draw(RecipeHolder<AirCondensingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        RenderSupport.renderBlock(
            guiGraphics,
            NPBlocks.PARTICLE_STABILIZER.getDefaultState(),
            81,
            40,
            12
        );
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(1, 1);
        guiGraphics.pose().scale(0.7943f, 0.7943f);
        guiGraphics.text(
            Minecraft.getInstance().font,
            Component.translatable("category.nekoplus.air_condensing.dimension", recipe.value().getDimension().getKey().identifier().toString()),
            0, 0,
            16777215
        );
        guiGraphics.pose().popMatrix();
        arrowIn.draw(guiGraphics, 54, 30);
        arrowOutputFromBelow.draw(guiGraphics, 92, 29);
        JeiSlotUtil.drawInputSlots(guiGraphics, slotDefault, 1);
        JeiSlotUtil.drawOutputSlots(guiGraphics, slotDefault, recipe.value().getResults().size());
    }

    @Override
    public IRecipeType<RecipeHolder<AirCondensingRecipe>> getRecipeType() {
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
        AirCondensingRecipe r = recipe.value();
        NPJeiSlotUtil.addInputSlots(builder, Collections.singletonList(Ingredient.of(NPItems.AIR_FILTER)));
        NPJeiSlotUtil.addOutputSlots(builder, r.getResultsAsItemStack());
    }

    @Override
    public int getWidth() {
        return 162;
    }

    @Override
    public int getHeight() {
        return 64;
    }
}

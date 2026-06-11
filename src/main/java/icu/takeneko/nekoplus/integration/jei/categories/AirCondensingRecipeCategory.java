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
import net.minecraft.util.ARGB;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

public class AirCondensingRecipeCategory extends NPBaseRecipeCategory<RecipeHolder<AirCondensingRecipe>> {
    public static final Component TITLE = Component.translatable("category.nekoplus.air_condensing");

    public AirCondensingRecipeCategory(IGuiHelper helper) {
        super(
            helper,
            TITLE,
            NPJeiPlugin.AIR_CONDENSING_TYPE,
            helper.createDrawableItemStack(NPBlocks.PARTICLE_STABILIZER.asStack()),
            162,
            64
        );
    }

    @Override
    public void draw(
        RecipeHolder<AirCondensingRecipe> recipe,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphicsExtractor guiGraphics,
        double mouseX,
        double mouseY
    ) {
        RenderSupport.renderBlock(
            guiGraphics,
            NPBlocks.PARTICLE_STABILIZER.getDefaultState(),
            68,
            35,
            24
        );
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(1, 1);
        guiGraphics.pose().scale(0.7943f, 0.7943f);
        guiGraphics.text(
            Minecraft.getInstance().font,
            Component.translatable(
                "category.nekoplus.air_condensing.dimension",
                recipe.value().getDimension().getKey().identifier().toString()
            ),
            0, 0,
            ARGB.color(255, 16777215)
        );
        guiGraphics.pose().popMatrix();
        arrowIn.draw(guiGraphics, 54, 30);
        arrowOutputFromBelow.draw(guiGraphics, 92, 29);
        JeiSlotUtil.drawInputSlots(guiGraphics, slotDefault, 1);
        JeiSlotUtil.drawOutputSlots(guiGraphics, slotDefault, recipe.value().getResults().size());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<AirCondensingRecipe> recipe, IFocusGroup focuses) {
        AirCondensingRecipe r = recipe.value();
        NPJeiSlotUtil.addInputSlots(builder, Collections.singletonList(Ingredient.of(NPItems.AIR_FILTER)));
        NPJeiSlotUtil.addOutputSlots(builder, r.getResultsAsItemStack());
    }
}

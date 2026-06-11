package icu.takeneko.nekoplus.integration.jei.categories;

import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import icu.takeneko.nekoplus.all.NPBlocks;
import lombok.Getter;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;

public abstract class NPBaseRecipeCategory<T> implements IRecipeCategory<T> {
    @Getter
    protected final IDrawable icon;
    protected final IDrawable slotDefault;
    protected final IDrawable slotProbability;
    protected final IDrawable arrowIn;
    protected final IDrawable arrowOut;
    protected final IDrawable arrowOutputFromBelow;
    protected final ITickTimer timer;
    @Getter
    protected final Component title;
    @Getter
    protected final IRecipeType<T> recipeType;
    @Getter
    protected final int width;
    @Getter
    protected final int height;

    public NPBaseRecipeCategory(
        IGuiHelper helper,
        Component title,
        IRecipeType<T> recipeType,
        IDrawable icon,
        int width,
        int height
    ) {
        this.icon = icon;
        this.slotDefault = JeiRenderHelper.getSlotDefault(helper);
        this.slotProbability = JeiRenderHelper.getSlotProbability(helper);
        this.arrowIn = JeiRenderHelper.getArrowInput(helper);
        this.arrowOut = JeiRenderHelper.getArrowOutput(helper);
        this.arrowOutputFromBelow = JeiRenderHelper.getArrowOutputFromBelow(helper);
        this.timer = helper.createTickTimer(30, 60, true);
        this.title = title;
        this.recipeType = recipeType;
        this.width = width;
        this.height = height;
    }
}

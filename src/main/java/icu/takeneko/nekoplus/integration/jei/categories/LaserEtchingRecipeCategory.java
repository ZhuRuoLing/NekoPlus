package icu.takeneko.nekoplus.integration.jei.categories;

import dev.dubhe.anvilcraft.block.RubyLaserBlock;
import dev.dubhe.anvilcraft.client.support.RenderSupport;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.block.HighEnergyLaserBlock;
import icu.takeneko.nekoplus.integration.jei.NPJeiPlugin;
import icu.takeneko.nekoplus.integration.jei.NPJeiSlotUtil;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.recipe.LaserEtchingRecipe;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LaserEtchingRecipeCategory implements IRecipeCategory<RecipeHolder<LaserEtchingRecipe>> {
    public static final Component TITLE = Component.translatable("category.nekoplus.laser_etching");

    private final IDrawable icon;
    private final IDrawable slotDefault;
    private final IDrawable slotProbability;
    private final IDrawable arrowIn;
    private final IDrawable arrowOut;
    private final IDrawable arrowOutputFromBelow;
    private final ITickTimer timer;
    private final ITickTimer timerLaserIterate;

    public LaserEtchingRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableItemStack(NPBlocks.PARTICLE_STABILIZER.asStack());
        this.slotDefault = JeiRenderHelper.getSlotDefault(helper);
        this.slotProbability = JeiRenderHelper.getSlotProbability(helper);
        this.arrowIn = JeiRenderHelper.getArrowInput(helper);
        this.arrowOut = JeiRenderHelper.getArrowOutput(helper);
        this.arrowOutputFromBelow = JeiRenderHelper.getArrowOutputFromBelow(helper);
        this.timer = helper.createTickTimer(30, 60, true);
        this.timerLaserIterate = helper.createTickTimer(40, 80, true);
    }

    @Override
    public void draw(RecipeHolder<LaserEtchingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);
        RenderSupport.renderBlock(
            guiGraphics,
            Blocks.ANVIL.defaultBlockState(),
            81,
            22 + anvilYOffset,
            20,
            12,
            RenderSupport.SINGLE_BLOCK
        );
        RenderSupport.renderBlock(
            guiGraphics,
            ModBlocks.STAMPING_PLATFORM.getDefaultState(),
            81,
            40,
            0,
            12,
            RenderSupport.SINGLE_BLOCK
        );

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(1, 1, 1);
        guiGraphics.pose().scale(0.7943f, 0.7943f, 0.8f);
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable("category.nekoplus.laser_etching.laser_requirement"),
            0, 0,
            16777215
        );
        guiGraphics.pose().popPose();

        BlockState blockState;
        if (timerLaserIterate.getValue() >= 40) {
            blockState = ModBlocks.RUBY_LASER.getDefaultState().setValue(RubyLaserBlock.FACING, Direction.UP);
        } else {
            blockState = NPBlocks.HIGH_ENERGY_LASER.getDefaultState().setValue(HighEnergyLaserBlock.FACING, Direction.UP);
        }

        RenderSupport.renderBlock(
            guiGraphics,
            blockState,
            81,
            57,
            0,
            12,
            RenderSupport.SINGLE_BLOCK
        );

        arrowIn.draw(guiGraphics, 54, 30);
        arrowOutputFromBelow.draw(guiGraphics, 92, 29);
        JeiSlotUtil.drawInputSlots(guiGraphics, slotDefault, 1);

        if (JeiRecipeUtil.isChance(List.of(recipe.value().output()))) {
            JeiSlotUtil.drawOutputSlots(guiGraphics, slotProbability, 1);
        } else {
            JeiSlotUtil.drawOutputSlots(guiGraphics, slotDefault, 1);
        }
    }

    @Override
    public RecipeType<RecipeHolder<LaserEtchingRecipe>> getRecipeType() {
        return NPJeiPlugin.LASER_ETCHING_TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<LaserEtchingRecipe> recipe, IFocusGroup focuses) {
        LaserEtchingRecipe r = recipe.value();
        NPJeiSlotUtil.addInputSlots(builder, Collections.singletonList(recipe.value().input()));
        JeiSlotUtil.addOutputSlots(builder, List.of(r.output()));
    }

    @Override
    public int getWidth() {
        return 162;
    }

    @Override
    public int getHeight() {
        return 80;
    }
}

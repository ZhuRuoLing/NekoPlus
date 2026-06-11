package icu.takeneko.nekoplus.integration.jei.categories;

import dev.dubhe.anvilcraft.block.laser.RubyLaserBlock;
import dev.dubhe.anvilcraft.client.support.RenderSupport;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.block.HighEnergyLaserBlock;
import icu.takeneko.nekoplus.integration.jei.NPJeiPlugin;
import icu.takeneko.nekoplus.integration.jei.NPJeiSlotUtil;
import icu.takeneko.nekoplus.recipe.LaserEtchingRecipe;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LaserEtchingRecipeCategory extends NPBaseRecipeCategory<RecipeHolder<LaserEtchingRecipe>> {
    public static final Component TITLE = Component.translatable("category.nekoplus.laser_etching");

    private final ITickTimer timerLaserIterate;

    public LaserEtchingRecipeCategory(IGuiHelper helper) {
        super(
            helper,
            TITLE,
            NPJeiPlugin.LASER_ETCHING_TYPE,
            helper.createDrawableItemStack(NPBlocks.HIGH_ENERGY_LASER.asStack()),
            162,
            80
        );
        this.timerLaserIterate = helper.createTickTimer(40, 80, true);
    }

    @Override
    public void draw(
        RecipeHolder<LaserEtchingRecipe> recipe,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphicsExtractor guiGraphics,
        double mouseX,
        double mouseY
    ) {
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(1, 1);
        guiGraphics.pose().scale(0.7943f, 0.7943f);
        guiGraphics.text(
            Minecraft.getInstance().font,
            Component.translatable("category.nekoplus.laser_etching.laser_requirement"),
            0, 0,
            ARGB.color(255, 16777215)
        );
        guiGraphics.pose().popMatrix();

        BlockState blockState;
        if (timerLaserIterate.getValue() >= 40) {
            blockState = ModBlocks.RUBY_LASER.getDefaultState().setValue(RubyLaserBlock.FACING, Direction.UP);
        } else {
            blockState = NPBlocks.HIGH_ENERGY_LASER.getDefaultState().setValue(
                HighEnergyLaserBlock.FACING,
                Direction.UP
            );
        }

        RenderSupport.renderBlock(
            guiGraphics,
            blockState,
            68,
            57,
            24
        );

        RenderSupport.renderBlock(
            guiGraphics,
            ModBlocks.STAMPING_PLATFORM.getDefaultState(),
            68,
            40,
            24
        );

        RenderSupport.renderBlock(
            guiGraphics,
            Blocks.ANVIL.defaultBlockState(),
            68,
            20 + anvilYOffset,
            24
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<LaserEtchingRecipe> recipe, IFocusGroup focuses) {
        LaserEtchingRecipe r = recipe.value();
        NPJeiSlotUtil.addInputSlots(builder, Collections.singletonList(recipe.value().input()));
        JeiSlotUtil.addOutputSlots(builder, List.of(r.output()));
    }
}

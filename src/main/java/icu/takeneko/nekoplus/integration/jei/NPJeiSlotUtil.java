package icu.takeneko.nekoplus.integration.jei;

import dev.anvilcraft.lib.v2.recipe.component.ChanceItemStack;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

public class NPJeiSlotUtil {
    public static void addSlotWithCount(
        IRecipeLayoutBuilder builder, int slotX, int slotY, Ingredient entry) {
        IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY);
        slot.addIngredients(entry);
    }

    public static void addInputSlots(
        IRecipeLayoutBuilder builder, List<Ingredient> mergedIngredients) {
        int inputSize = mergedIngredients.size();
        if (inputSize == 0) return;
        if (inputSize == 1) {
            Ingredient ingredient = mergedIngredients.getFirst();
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, 21, 24);
            slot.addIngredients(ingredient);
        } else if (inputSize <= 4) {
            int startX = 11;
            int startY = 15;
            for (int index = 0; index < inputSize; index++) {
                int row = index / 2;
                int col = index % 2;
                addSlotWithCount(builder, startX + 19 * col, startY + 19 * row, mergedIngredients.get(index));
            }
        } else if (inputSize <= 6) {
            int startX = 2;
            int startY = 15;
            for (int index = 0; index < inputSize; index++) {
                int row = index / 3;
                int col = index % 3;
                addSlotWithCount(builder, startX + 19 * col, startY + 19 * row, mergedIngredients.get(index));
            }
        } else {
            int startX = 1;
            int startY = 6;
            for (int index = 0; index < inputSize; index++) {
                if (index > 9) break;
                int row = index / 3;
                int col = index % 3;
                addSlotWithCount(builder, startX + 19 * col, startY + 19 * row, mergedIngredients.get(index));
            }
        }
    }

    public static void addOutputSlots(IRecipeLayoutBuilder builder, List<ItemStack> results) {
        int outputSize = results.size();
        if (outputSize == 0) return;
        if (outputSize == 1) {
            ItemStack stack = results.getFirst();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 24)
                .addItemStack(stack);
        } else if (outputSize <= 4) {
            int startX = 117;
            int startY = 15;
            for (int index = 0; index < outputSize; index++) {
                int row = index / 2;
                int col = index % 2;
                ItemStack stack = results.get(index);
                builder.addSlot(RecipeIngredientRole.OUTPUT, startX + 19 * col, startY + 19 * row)
                    .addItemStack(stack);
            }
        } else if (outputSize <= 6) {
            int startX = 108;
            int startY = 15;
            for (int index = 0; index < outputSize; index++) {
                int row = index / 3;
                int col = index % 3;
                ItemStack stack = results.get(index);
                builder.addSlot(RecipeIngredientRole.OUTPUT, startX + 19 * col, startY + 19 * row)
                    .addItemStack(stack);
            }
        } else {
            int startX = 108;
            int startY = 6;
            for (int index = 0; index < outputSize; index++) {
                if (index > 9) break;
                int row = index / 3;
                int col = index % 3;
                ItemStack stack = results.get(index);
                builder.addSlot(RecipeIngredientRole.OUTPUT, startX + 19 * col, startY + 19 * row)
                    .addItemStack(stack);
            }
        }
    }
}

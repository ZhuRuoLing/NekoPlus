package icu.takeneko.highenergyanvilology.foundation.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class SingleRecipeInput<T> implements RecipeInput {
    private final T input;

    private SingleRecipeInput(T item) {
        this.input = item;
    }

    @Override
    public ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }

    public T unwrap() {
        return input;
    }

    public static <T> SingleRecipeInput<T> of(T entry) {
        return new SingleRecipeInput<>(entry);
    }
}

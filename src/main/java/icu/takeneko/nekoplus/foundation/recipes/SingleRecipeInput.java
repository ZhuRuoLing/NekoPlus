package icu.takeneko.nekoplus.foundation.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.Nullable;

public class SingleRecipeInput<T> implements RecipeInput {
    @Nullable
    private final T input;

    private SingleRecipeInput(@Nullable T item) {
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
        if (input == null) throw new IllegalArgumentException("Invalid recipe input: null");
        return input;
    }

    public static <T> SingleRecipeInput<T> of(T entry) {
        return new SingleRecipeInput<>(entry);
    }

    @Override
    public boolean isEmpty() {
        return input == null;
    }
}

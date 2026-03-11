package icu.takeneko.nekoplus.block.tile.logic.stabilizer;

import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerSlice;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ParticleStabilizerLogicHost {

    ItemStack getTriggerItem();

    ItemStack tryConsumeTriggerItem();

    NPItemHandlerSlice getOutputItemHandler();

    BlockPos getBlockPos();

    boolean hasValidWorkingState();

    void resetCooldown();

    void resetState();

    Level getLevel();

    void setProgress(int progress);

    void setMaxProgress(int value);

    int getProgress();

    AirCondensingRecipe getCurrentRecipe();

    void setCurrentRecipe(AirCondensingRecipe recipe);
}

package icu.takeneko.highenergyanvilology.block.entity.logic.stabilizer;

import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandlerSlice;
import icu.takeneko.highenergyanvilology.recipes.AirCondensingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ParticleStabilizerLogicHost {

    ItemStack getTriggerItem();

    ItemStack tryConsumeTriggerItem();

    HEItemHandlerSlice getOutputItemHandler();

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

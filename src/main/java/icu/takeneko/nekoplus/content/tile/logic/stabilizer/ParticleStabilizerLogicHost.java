package icu.takeneko.nekoplus.content.tile.logic.stabilizer;

import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerSlice;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;

public interface ParticleStabilizerLogicHost {

    ItemResource getTriggerResource();

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

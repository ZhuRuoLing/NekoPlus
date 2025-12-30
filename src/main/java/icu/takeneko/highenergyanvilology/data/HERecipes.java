package icu.takeneko.highenergyanvilology.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.recipe.mineral.MineralFountainRecipe;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.all.HEBlocks;
import net.minecraft.world.level.block.Blocks;

public class HERecipes {
    public static void addRecipes(RegistrateRecipeProvider provider) {
        MineralFountainRecipe.builder()
            .fromBlock(Blocks.NETHERRACK)
            .needBlock(HEBlocks.NETHERITE_SCRAP_BLOCK.get())
            .toBlock(Blocks.ANCIENT_DEBRIS)
            .save(provider, HEAnvilology.location("mineral_fountain/ancient_debris"));
    }
}

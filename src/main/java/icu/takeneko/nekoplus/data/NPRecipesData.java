package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.providers.generators.RegistrumRecipeProvider;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.BlockCompressRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.BlockSmearRecipe;
import dev.dubhe.anvilcraft.recipe.mineral.MineralFountainRecipe;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

public class NPRecipesData {
    public static void addRecipes(RegistrumRecipeProvider provider) {
        MineralFountainRecipe.builder()
            .fromBlock(Blocks.NETHERRACK)
            .needBlock(NPBlocks.NETHERITE_SCRAP_BLOCK.get())
            .toBlock(Blocks.ANCIENT_DEBRIS)
            .save(provider, NekoPlus.location("mineral_fountain/ancient_debris"));

        AirCondensingRecipe.builder()
            .dimension(provider.resolve(BuiltinDimensionTypes.END))
            .results(List.of(new ItemStackTemplate(ModItems.LEVITATION_POWDER.asItem(), 4)))
            .probability(ConstantValue.exactly(0.8f))
            .ticks(10)
            .build()
            .save(provider, NekoPlus.location("air_condensing/end"));

        BlockCompressRecipe.builder()
            .input(ModBlocks.GUNPOWER_BLOCK.get())
            .input(ModBlocks.PIEZOELECTRIC_CRYSTAL.get())
            .result(NPBlocks.BLAST_CRYSTAL.get())
            .save(provider, NekoPlus.location("block_smear/blast_crystal"));
    }
}

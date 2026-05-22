package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.providers.generators.RegistrumRecipeProvider;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.mineral.MineralFountainRecipe;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import net.minecraft.world.item.ItemStack;
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
            .results(List.of(new ItemStack(ModItems.LEVITATION_POWDER.asItem(), 4)))
            .probability(ConstantValue.exactly(0.8f))
            .ticks(10)
            .build()
            .save(provider, NekoPlus.location("air_condensing/end"));

//        MultiblockConversionRecipe.builder()
//            .inputLayer("  AAA  ", " ABBBA ", "CBBBBBD", "CBBBBBD", "CBBBBBD", " ABBBA ", "  EFE  ")
//            .inputLayer(" ABBBA ", "A     A", "B     B", "B     B", "B     B", "A     A", " ABBBA ")
//            .inputLayer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
//            .inputLayer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
//            .inputLayer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
//            .inputLayer(" ABBBA ", "A     A", "B     B", "B     B", "B     B", "A     A", " ABBBA ")
//            .inputLayer("  AAA  ", " ABBBA ", "ABBBBBA", "ABBBBBA", "ABBBBBA", " ABBBA ", "  AAA  ")
//            .symbol('A', "nekoplus:titanium_alloy_block")
//            .symbol('B', "anvilcraft:ember_glass")
//            .symbol('C', BlockPredicateWithState.of("nekoplus:energy_output_hatch")
//                .hasState("facing", "west")
//            )
//            .symbol('D', BlockPredicateWithState.of("nekoplus:item_output_hatch")
//                .hasState("facing", "east")
//            )
//            .symbol('E', BlockPredicateWithState.of("nekoplus:item_input_hatch")
//                .hasState("facing", "south")
//            )
//            .symbol('F', BlockPredicateWithState.of("nekoplus:fusion_reactor_controller")
//                .hasState("facing", "south")
//            )
//            .outputLayer("A")
//            .save(provider, HEAnvilology.location("fission_reactor"));

//        MultiblockRecipe.builder("nekoplus:fusion_reactor_controller", 1)
//            .layer("  AAA  ", " ABBBA ", "CBBBBBD", "CBBBBBD", "CBBBBBD", " ABBBA ", "  EFE  ")
//            .layer(" ABBBA ", "A     A", "B     B", "B     B", "B     B", "A     A", " ABBBA ")
//            .layer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
//            .layer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
//            .layer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
//            .layer(" ABBBA ", "A     A", "B     B", "B     B", "B     B", "A     A", " ABBBA ")
//            .layer("  AAA  ", " ABBBA ", "ABBBBBA", "ABBBBBA", "ABBBBBA", " ABBBA ", "  AAA  ")
//            .symbol('A', "nekoplus:titanium_alloy_block")
//            .symbol('B', "anvilcraft:ember_glass")
//            .symbol('C', BlockPredicateWithState.of("nekoplus:energy_output_hatch")
//                .hasState("facing", "west")
//            )
//            .symbol('D', BlockPredicateWithState.of("nekoplus:item_output_hatch")
//                .hasState("facing", "east")
//            )
//            .symbol('E', BlockPredicateWithState.of("nekoplus:item_input_hatch")
//                .hasState("facing", "south")
//            )
//            .symbol('F', BlockPredicateWithState.of("nekoplus:fusion_reactor_controller")
//                .hasState("facing", "south")
//            )
//            .save(provider);
    }
}

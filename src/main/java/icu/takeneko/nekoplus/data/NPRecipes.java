package icu.takeneko.nekoplus.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.recipe.mineral.MineralFountainRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPredicateWithState;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockRecipe;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import net.minecraft.world.level.block.Blocks;

public class NPRecipes {
    public static void addRecipes(RegistrateRecipeProvider provider) {
        MineralFountainRecipe.builder()
            .fromBlock(Blocks.NETHERRACK)
            .needBlock(NPBlocks.NETHERITE_SCRAP_BLOCK.get())
            .toBlock(Blocks.ANCIENT_DEBRIS)
            .save(provider, NekoPlus.location("mineral_fountain/ancient_debris"));

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

        MultiblockRecipe.builder("nekoplus:fusion_reactor_controller", 1)
            .layer("  AAA  ", " ABBBA ", "CBBBBBD", "CBBBBBD", "CBBBBBD", " ABBBA ", "  EFE  ")
            .layer(" ABBBA ", "A     A", "B     B", "B     B", "B     B", "A     A", " ABBBA ")
            .layer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
            .layer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
            .layer("ABBBBBA", "B     B", "B     B", "B     B", "B     B", "B     B", "ABBBBBA")
            .layer(" ABBBA ", "A     A", "B     B", "B     B", "B     B", "A     A", " ABBBA ")
            .layer("  AAA  ", " ABBBA ", "ABBBBBA", "ABBBBBA", "ABBBBBA", " ABBBA ", "  AAA  ")
            .symbol('A', "nekoplus:titanium_alloy_block")
            .symbol('B', "anvilcraft:ember_glass")
            .symbol('C', BlockPredicateWithState.of("nekoplus:energy_output_hatch")
                .hasState("facing", "west")
            )
            .symbol('D', BlockPredicateWithState.of("nekoplus:item_output_hatch")
                .hasState("facing", "east")
            )
            .symbol('E', BlockPredicateWithState.of("nekoplus:item_input_hatch")
                .hasState("facing", "south")
            )
            .symbol('F', BlockPredicateWithState.of("nekoplus:fusion_reactor_controller")
                .hasState("facing", "south")
            )
            .save(provider);
    }
}

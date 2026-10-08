package icu.takeneko.nekoplus.data.provider;

import dev.anvilcraft.lib.v2.registrum.providers.generators.RegistrumRecipeProvider;
import dev.anvilcraft.lib.v2.registrum.util.DataIngredient;
import dev.dubhe.anvilcraft.block.state.Cube3x3PartHalf;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.BlockCompressRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.BlockCrushRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.MeshRecipe;
import dev.dubhe.anvilcraft.recipe.mineral.MineralFountainRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPredicateWithState;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockConversionRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockRecipe;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.block.HugeBatteryBlock;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.recipe.ModuleAssembleRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

public class NPRecipesGen {
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

        SpecialRecipeBuilder.special(() -> ModuleAssembleRecipe.INSTANCE)
            .save(provider, ResourceKey.create(Registries.RECIPE, NekoPlus.location("crafting/module_assemble")));

        addBasaltRecipes(provider);
        addMultiBlockRecipes(provider);
    }

    private static void addBasaltRecipes(RegistrumRecipeProvider provider) {
        BlockCrushRecipe.builder()
            .input(Blocks.BASALT)
            .result(NPBlocks.BASALT_SAND.get())
            .save(provider, NekoPlus.location("block_crush/basalt_sand"));

        // TODO: Tune basalt sand mesh probabilities after balancing the production line.
        MeshRecipe.builder()
            .requires(NPBlocks.BASALT_SAND)
            .result(NPBlocks.BASALT_SAND, 0.5F)
            .result(Items.IRON_NUGGET, 0.25F)
            .result(NPItems.OLIVINE, 0.15F)
            .result(NPItems.NICKEL_NUGGET, 0.1F)
            .save(provider, NekoPlus.location("mesh/basalt_sand"));

        provider.square(
            DataIngredient.items(NPItems.OLIVINE.get()), RecipeCategory.BUILDING_BLOCKS,
            NPBlocks.FOUNDRY_SAND, true
        );
        provider.storage(NPItems.NICKEL_NUGGET, RecipeCategory.MISC, NPItems.NICKEL_INGOT);
        provider.storage(NPItems.NICKEL_INGOT, RecipeCategory.BUILDING_BLOCKS, NPBlocks.NICKEL_BLOCK);
        provider.storage(NPItems.RAW_NICKEL, RecipeCategory.BUILDING_BLOCKS, NPBlocks.RAW_NICKEL_BLOCK);
        provider.smeltingAndBlasting(
            DataIngredient.items(NPItems.RAW_NICKEL.get()), RecipeCategory.MISC,
            CookingBookCategory.MISC, NPItems.NICKEL_INGOT, 0.7F
        );
        provider.smeltingAndBlasting(
            DataIngredient.items(NPBlocks.DEEPSLATE_NICKEL_ORE.get()), RecipeCategory.MISC,
            CookingBookCategory.MISC, NPItems.NICKEL_INGOT, 0.7F
        );
    }

    public static void addMultiBlockRecipes(RegistrumRecipeProvider provider) {
        MultiblockRecipe.builder(NPBlocks.HUGE_BATTERY)
            .layer("AAA", "A A", "AAA")
            .layer("A A", " B ", "A A")
            .layer("AAA", "A A", "AAA")
            .symbol('A', ModBlocks.MENGER_SPONGE)
            .symbol('B', NPBlocks.BATTERY)
            .save(provider, NekoPlus.location("multiblock/huge_battery_item"));

        MultiblockConversionRecipe.builder()
            .inputLayer("AAA", "A A", "AAA")
            .inputLayer("A A", " B ", "A A")
            .inputLayer("AAA", "A A", "AAA")
            .inputSymbol('A', ModBlocks.MENGER_SPONGE)
            .inputSymbol('B', NPBlocks.BATTERY)
            .outputLayer("ABC", "DEF", "GHI")
            .outputLayer("JKL", "MNO", "PQR")
            .outputLayer("STU", "VWX", "YZ[")
            .outputSymbol(
                'A',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_WN)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'B',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_N)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'C',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_EN)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'D',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_W)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'E',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_CENTER)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'F',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_E)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'G',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_WS)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'H',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_S)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'I',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.BOTTOM_ES)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'J',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_WN)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'K',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_N)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'L',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_EN)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'M',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_W)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'N',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_CENTER)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'O',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_E)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'P',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_WS)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'Q',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_S)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'R',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.MID_ES)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'S',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_WN)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'T',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_N)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'U',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_EN)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'V',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_W)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'W',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_CENTER)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'X',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_E)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'Y',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_WS)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                'Z',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_S)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .outputSymbol(
                '[',
                BlockPredicateWithState.of(NPBlocks.HUGE_BATTERY)
                    .hasState(HugeBatteryBlock.PART, Cube3x3PartHalf.TOP_ES)
                    .hasState(HugeBatteryBlock.DISCHARGING, false)
                    .hasState(HugeBatteryBlock.OVERLOAD, true)
            )
            .save(provider, NekoPlus.location("multiblock/huge_battery_blocks"));
    }
}

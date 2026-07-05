package icu.takeneko.nekoplus.data.provider;

import dev.anvilcraft.lib.v2.registrum.providers.generators.RegistrumRecipeProvider;
import dev.dubhe.anvilcraft.block.state.Cube3x3PartHalf;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.BlockCompressRecipe;
import dev.dubhe.anvilcraft.recipe.mineral.MineralFountainRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPredicateWithState;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockConversionRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockRecipe;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.block.HugeBatteryBlock;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.recipe.ModuleAssembleRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
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

        addMultiBlockRecipes(provider);
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

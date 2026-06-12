package icu.takeneko.nekoplus.all;

import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.data.AnvilCraftDatagen;
import dev.dubhe.anvilcraft.data.recipe.RegistrumBlockRecipeLoader;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.block.state.Cube323PartHalf;
import dev.dubhe.anvilcraft.util.registrater.DataGenUtil;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.block.BlastCrystalBlock;
import icu.takeneko.nekoplus.block.CatAnvilBlock;
import icu.takeneko.nekoplus.block.MineralFountainPressurizerBlock;
import icu.takeneko.nekoplus.block.HighEnergyLaserBlock;
import icu.takeneko.nekoplus.block.ParticleStabilizerBlock;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.block.ShulkerHatchBlock;
import icu.takeneko.nekoplus.block.StellarEngineBlock;
import icu.takeneko.nekoplus.block.TardisBlock;
import icu.takeneko.nekoplus.block.FatAnvilBlock;
import icu.takeneko.nekoplus.block.property.Part3;
import icu.takeneko.nekoplus.data.NPBlockStateDispatches;
import icu.takeneko.nekoplus.item.ShulkerHatchBlockItem;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public class NPBlocks {

    static {
        NekoPlus.REGISTRUM.defaultCreativeTab(NPCreativeTabs.TAB.getKey());
    }

    public static final BlockEntry<CatAnvilBlock> CAT_ANVIL = NekoPlus.REGISTRUM
        .block("cat_anvil", CatAnvilBlock::new)
        .initialProperties(() -> Blocks.CAKE)
        .properties(p -> p
            .sound(new DeferredSoundType(
                1f,
                1f,
                NPSoundEvents.CAT_ANVIL_BREAK,
                NPSoundEvents.CAT_ANVIL_STEP,
                NPSoundEvents.CAT_ANVIL_PLACE,
                NPSoundEvents.CAT_ANVIL_HIT,
                NPSoundEvents.CAT_ANVIL_FALL
            ))
        ).tag(
            BlockTags.ANVIL,
            ModBlockTags.NON_MAGNETIC,
            ModBlockTags.CANT_BROKEN_ANVIL,
            BlockTags.MINEABLE_WITH_PICKAXE
        )
        .blockstate(NPBlockStateDispatches::catAnvil)
        .item()
        .build()
        .register();

    public static final BlockEntry<Block> ROYAL_STEEL_CASING = NekoPlus.REGISTRUM
        .block("royal_steel_casing", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p.strength(2f, 6.0F))
        .item()
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.MISC, ctx.get())
                .pattern(" A ")
                .pattern("A A")
                .pattern(" A ")
                .define('A', ModItems.ROYAL_STEEL_INGOT)
                .unlockedBy(
                    "has_" + prov.safeName(ModItems.ROYAL_STEEL_INGOT),
                    prov.has(ModItems.ROYAL_STEEL_INGOT)
                )
                .save(prov);
        })
        .build()
        .register();

    public static final BlockEntry<ParticleStabilizerBlock> PARTICLE_STABILIZER = NekoPlus.REGISTRUM
        .block("particle_stabilizer", ParticleStabilizerBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(BlockBehaviour.Properties::noOcclusion)
        .blockstate(NPBlockStateDispatches::particleStabilizer)
        .item()
        .properties(p -> p.rarity(Rarity.UNCOMMON))
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.MISC, ctx.get())
                .pattern("ADA")
                .pattern("BCB")
                .pattern("AEA")
                .define('A', ModItemTags.GEMS_TOPAZ)
                .define('B', ModBlocks.EMBER_GLASS)
                .define('C', ROYAL_STEEL_CASING)
                .define('D', NPItems.CRYOCOOLER)
                .define('E', ModItems.CIRCUIT_BOARD)
                .unlockedBy("has_" + ModItemTags.GEMS_TOPAZ.location().getPath(), prov.has(ModItemTags.GEMS_TOPAZ))
                .unlockedBy("has_" + ModBlocks.EMBER_GLASS.getRegisteredName(), prov.has(ModBlocks.EMBER_GLASS))
                .unlockedBy("has_" + ROYAL_STEEL_CASING.getRegisteredName(), prov.has(ROYAL_STEEL_CASING))
                .unlockedBy("has_" + NPItems.CRYOCOOLER.getRegisteredName(), prov.has(NPItems.CRYOCOOLER))
                .unlockedBy("has_" + ModItems.CIRCUIT_BOARD.getRegisteredName(), prov.has(ModItems.CIRCUIT_BOARD))
                .save(prov);
        })
        .model(DataGenUtil::blockItem)
        .build()
        .register();

    public static final BlockEntry<HighEnergyLaserBlock> HIGH_ENERGY_LASER = NekoPlus.REGISTRUM
        .block("high_energy_laser", HighEnergyLaserBlock::new)
        .blockstate(NPBlockStateDispatches::highEnergyLaser1)
        .item()
        .model(DataGenUtil::blockItem)
        .properties(p -> p.rarity(Rarity.RARE))
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.REDSTONE, ctx.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern("DED")
                .define('A', ModItems.TIN_NUGGET)
                .define('B', NPItems.CARBON_DIOXIDE_LASER_TUBE)
                .define('C', NPItems.TITANIUM_ALLOY_INGOT)
                .define('D', ModItems.TRANSCENDIUM_INGOT)
                .define('E', NPItems.INTEGRATED_CHIP_CIRCUIT_BOARD)
                .unlockedBy(
                    "has_" + ModItems.TIN_NUGGET.getRegisteredName(),
                    prov.has(ModItems.TIN_NUGGET)
                )
                .unlockedBy(
                    "has_" + NPItems.CARBON_DIOXIDE_LASER_TUBE.getRegisteredName(),
                    prov.has(NPItems.CARBON_DIOXIDE_LASER_TUBE)
                )
                .unlockedBy(
                    "has_" + NPItems.TITANIUM_ALLOY_INGOT.getRegisteredName(),
                    prov.has(NPItems.TITANIUM_ALLOY_INGOT)
                )
                .unlockedBy(
                    "has_" + NPItems.INTEGRATED_CHIP_CIRCUIT_BOARD.getRegisteredName(),
                    prov.has(NPItems.INTEGRATED_CHIP_CIRCUIT_BOARD)
                )
                .save(prov);
        })
        .build()
        .register();

    public static final BlockEntry<ShulkerHatchBlock> SHULKER_HATCH = NekoPlus.REGISTRUM
        .block("shulker_hatch", ShulkerHatchBlock::new)
        .initialProperties(ROYAL_STEEL_CASING)
        .blockstate(NPBlockStateDispatches::shulkerHatch)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .item(ShulkerHatchBlockItem::new)
        .recipe((ctx, prov) ->
            ShapelessRecipeBuilder.shapeless(prov.getItems(), RecipeCategory.REDSTONE, ctx.get(), 4)
                .requires(Ingredient.of(NPBlocks.ROYAL_STEEL_CASING))
                .requires(Ingredient.of(ModBlocks.CHUTE))
                .unlockedBy(
                    "has_" + NPBlocks.ROYAL_STEEL_CASING.getRegisteredName(),
                    prov.has(NPBlocks.ROYAL_STEEL_CASING)
                )
                .unlockedBy(
                    "has_" + ModBlocks.CHUTE.getRegisteredName(),
                    prov.has(ModBlocks.CHUTE)
                )
                .save(prov, prov.safeKey(ctx.getId()))
        )
        .model(DataGenUtil::blockItem)
        .build()
        .register();

    public static final BlockEntry<ProgrammableLogicGateBlock> PROGRAMMABLE_LOGIC_GATE = NekoPlus.REGISTRUM
        .block("programmable_logic_gate", ProgrammableLogicGateBlock::new)
        .properties(properties -> properties.strength(3.0F, 3.5F).sound(SoundType.STONE).noOcclusion())
        .blockstate(NPBlockStateDispatches::programmableLogicGate)
        .item()
        .recipe((ctx, prov) ->
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.REDSTONE, ctx.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern("DDD")
                .define('A', Ingredient.of(Items.REDSTONE_TORCH))
                .define('B', ModBlocks.ADVANCED_COMPARATOR)
                .define('C', NPItems.ADVANCED_PROCESSOR)
                .define('D', Ingredient.of(Items.IRON_INGOT))
                .unlockedBy(
                    "has_" + BuiltInRegistries.ITEM.getKey(Items.REDSTONE_TORCH).getPath(),
                    prov.has(Items.REDSTONE_TORCH)
                )
                .unlockedBy(
                    "has_" + ModBlocks.ADVANCED_COMPARATOR.getRegisteredName(),
                    prov.has(ModBlocks.ADVANCED_COMPARATOR)
                )
                .unlockedBy(
                    "has_" + NPItems.ADVANCED_PROCESSOR.getRegisteredName(),
                    prov.has(NPItems.ADVANCED_PROCESSOR)
                )
                .unlockedBy(
                    "has_" + BuiltInRegistries.ITEM.getKey(Items.IRON_INGOT).getPath(),
                    prov.has(Items.IRON_INGOT)
                )
                .save(prov, prov.safeKey(ctx.getId()))
        )
        .model(DataGenUtil::onlyInfo)
        .build()
        .register();

    public static final BlockEntry<BlastCrystalBlock> BLAST_CRYSTAL = NekoPlus.REGISTRUM
        .block("blast_crystal", BlastCrystalBlock::new)
        .initialProperties(() -> Blocks.STONE)
        .properties(it -> it.noOcclusion().explosionResistance(1200).sound(SoundType.GLASS))
        .blockstate(DataGenUtil::onlyState)
        .item()
        .model(DataGenUtil::blockItem)
        .build()
        .register();

    public static final BlockEntry<BlastCrystalBlock> CRACKED_BLAST_CRYSTAL = NekoPlus.REGISTRUM
        .block("cracked_blast_crystal", it -> new BlastCrystalBlock(it, BlastCrystalBlock.CrackStage.CRACKED))
        .initialProperties(() -> Blocks.STONE)
        .properties(it -> it.noOcclusion().explosionResistance(1200).sound(SoundType.GLASS))
        .blockstate(DataGenUtil::onlyState)
        .item()
        .model(DataGenUtil::blockItem)
        .build()
        .register();

    public static final BlockEntry<BlastCrystalBlock> DAMAGED_BLAST_CRYSTAL = NekoPlus.REGISTRUM
        .block("damaged_blast_crystal", it -> new BlastCrystalBlock(it, BlastCrystalBlock.CrackStage.DAMAGED))
        .initialProperties(() -> Blocks.STONE)
        .properties(it -> it.noOcclusion().explosionResistance(1200).sound(SoundType.GLASS))
        .blockstate(DataGenUtil::onlyState)
        .item()
        .model(DataGenUtil::blockItem)
        .build()
        .register();

    public static final BlockEntry<Block> TITANIUM_ALLOY_BLOCK = NekoPlus.REGISTRUM
        .block("titanium_alloy_block", Block::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .tag(Tags.Blocks.STORAGE_BLOCKS, NPTags.Blocks.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .item()
        .tag(Tags.Items.STORAGE_BLOCKS, NPTags.Items.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .build()
        .register();

    public static final BlockEntry<Block> CUT_TITANIUM_ALLOY_BLOCK = NekoPlus.REGISTRUM
        .block("cut_titanium_alloy_block", Block::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .item()
        .recipe((ctx, prov) -> {
            SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(NPBlocks.TITANIUM_ALLOY_BLOCK),
                    RecipeCategory.BUILDING_BLOCKS,
                    ctx.get(),
                    4
                )
                .unlockedBy("hasitem", AnvilCraftDatagen.has(prov.getItems(), NPBlocks.TITANIUM_ALLOY_BLOCK))
                .save(prov, recipeLocation("stonecutting/cut_titanium_alloy_block"));
        })
        .build()
        .register();

    public static final BlockEntry<SlabBlock> CUT_TITANIUM_ALLOY_SLAB = NekoPlus.REGISTRUM
        .block("cut_titanium_alloy_slab", SlabBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .blockstate(() -> DataGenUtil.slabBlock(
            _ -> new Material(NekoPlus.location("block/cut_titanium_alloy_block")),
            _ -> new Material(NekoPlus.location("block/cut_titanium_alloy_block")),
            _ -> new Material(NekoPlus.location("block/cut_titanium_alloy_block")),
            _ -> NekoPlus.location("block/cut_titanium_alloy_block")
        ))
        .tag(BlockTags.SLABS)
        .item()
        .tag(ItemTags.SLABS)
        .recipe((ctx, prov) -> {
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(NPBlocks.TITANIUM_ALLOY_BLOCK),
                RecipeCategory.BUILDING_BLOCKS,
                ctx.get(),
                8
            ).unlockedBy(
                AnvilCraftDatagen.hasItem(NPBlocks.TITANIUM_ALLOY_BLOCK),
                AnvilCraftDatagen.has(prov.getItems(), NPBlocks.TITANIUM_ALLOY_BLOCK)
            ).save(
                prov,
                recipeLocation("stonecutting/cut_titanium_alloy_slab_from_titanium_alloy_block")
            );

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(NPBlocks.CUT_TITANIUM_ALLOY_BLOCK),
                RecipeCategory.BUILDING_BLOCKS,
                ctx.get(),
                2
            ).unlockedBy(
                AnvilCraftDatagen.hasItem(NPBlocks.CUT_TITANIUM_ALLOY_BLOCK),
                AnvilCraftDatagen.has(prov.getItems(), NPBlocks.CUT_TITANIUM_ALLOY_BLOCK)
            ).save(
                prov,
                recipeLocation("stonecutting/cut_titanium_alloy_slab_from_cut_titanium_alloy_block")
            );
        })
        .build()
        .register();


    public static final BlockEntry<StairBlock> CUT_TITANIUM_ALLOY_STAIR = NekoPlus.REGISTRUM
        .block(
            "cut_titanium_alloy_stair",
            properties -> new StairBlock(NPBlocks.CUT_TITANIUM_ALLOY_BLOCK.getDefaultState(), properties)
        )
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .blockstate(() -> DataGenUtil.stairsBlock(
            _ -> new Material(NekoPlus.location("block/cut_titanium_alloy_block")),
            _ -> new Material(NekoPlus.location("block/cut_titanium_alloy_block")),
            _ -> new Material(NekoPlus.location("block/cut_titanium_alloy_block"))
        ))
        .tag(BlockTags.STAIRS)
        .item()
        .tag(ItemTags.STAIRS)
        .recipe((ctx, prov) -> {
            SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(NPBlocks.TITANIUM_ALLOY_BLOCK),
                    RecipeCategory.BUILDING_BLOCKS,
                    ctx.get(),
                    4
                ).unlockedBy(
                    AnvilCraftDatagen.hasItem(NPBlocks.TITANIUM_ALLOY_BLOCK),
                    AnvilCraftDatagen.has(prov.getItems(), NPBlocks.TITANIUM_ALLOY_BLOCK)
                )
                .save(
                    prov,
                    recipeLocation("stonecutting/cut_royal_steel_slab_from_royal_steel_block")
                );

            SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(NPBlocks.CUT_TITANIUM_ALLOY_BLOCK),
                    RecipeCategory.BUILDING_BLOCKS,
                    ctx.get(),
                    1
                ).unlockedBy(
                    AnvilCraftDatagen.hasItem(NPBlocks.CUT_TITANIUM_ALLOY_BLOCK),
                    AnvilCraftDatagen.has(prov.getItems(), NPBlocks.CUT_TITANIUM_ALLOY_BLOCK)
                )
                .save(
                    prov,
                    recipeLocation("stonecutting/cut_royal_steel_slab_from_cut_royal_steel_block")
                );
        })
        .build()
        .register();

    public static final BlockEntry<FatAnvilBlock> TITANIUM_ALLOY_ANVIL = NekoPlus.REGISTRUM
        .block("titanium_alloy_anvil", FatAnvilBlock::new)
        .initialProperties(() -> Blocks.ANVIL)
        .properties(p -> p.pushReaction(PushReaction.NORMAL))
        .tag(
            ModBlockTags.ANVIL_TIER_0,
            ModBlockTags.ANVIL_TIER_1,
            BlockTags.ANVIL,
            ModBlockTags.CANT_BROKEN_ANVIL,
            BlockTags.MINEABLE_WITH_PICKAXE
        )
        .properties(p -> p.isValidSpawn(Blocks::never).strength(5.0f, 1200f))
        .blockstate(NPBlockStateDispatches::titaniumAlloyAnvil)
        .item()
        .tag(ItemTags.ANVIL)
        .recipe((src, ctx) -> {
            ShapedRecipeBuilder.shaped(ctx.getItems(), RecipeCategory.TOOLS, src.get())
                .pattern("AAA")
                .pattern(" B ")
                .pattern("BBB")
                .define('A', TITANIUM_ALLOY_BLOCK)
                .define('B', NPItems.TITANIUM_ALLOY_INGOT)
                .unlockedBy(
                    "has_" + ctx.safeName(TITANIUM_ALLOY_BLOCK),
                    ctx.has(TITANIUM_ALLOY_BLOCK)
                )
                .unlockedBy(
                    "has_" + ctx.safeName(NPItems.TITANIUM_ALLOY_INGOT),
                    ctx.has(NPItems.TITANIUM_ALLOY_INGOT)
                )
                .save(ctx);
        })
        .build()
        .register();

    public static final BlockEntry<Block> NETHERITE_SCRAP_BLOCK = NekoPlus.REGISTRUM
        .block("netherite_scrap_block", Block::new)
        .defaultBlockstate()
        .item()
        .recipe((ctx, prov) -> {
            prov.storage(() -> Items.NETHERITE_SCRAP, RecipeCategory.MISC, ctx);
        })
        .build()
        .register();

    public static final BlockEntry<StellarEngineBlock> STELLAR_ENGINE = NekoPlus.REGISTRUM
        .block("stellar_engine", StellarEngineBlock::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p.sound(SoundType.METAL)
            .noOcclusion()
            .emissiveRendering((_, _, _) -> true)
        )
        .blockstate(DataGenUtil::onlyState)
        .item()
        .properties(p -> p.rarity(Rarity.EPIC))
        .model(DataGenUtil::onlyInfo)
        .build()
        .register();

    public static final BlockEntry<TardisBlock> TARDIS = NekoPlus.REGISTRUM
        .block("tardis", TardisBlock::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p.sound(SoundType.METAL)
            .noOcclusion()
            .explosionResistance(114514)
        )
        .blockstate(DataGenUtil::onlyState)
        .loot((tab, block) -> tab.add(
            block,
            LootTable.lootTable()
                .withPool(
                    tab.applyExplosionCondition(
                        block,
                        LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(
                                LootItem.lootTableItem(block)
                                    .when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(
                                                TardisBlock.PART,
                                                Part3.BOTTOM
                                            ))
                                    )
                            )
                    )
                )
        ))
        .item()
        .properties(p -> p.rarity(Rarity.EPIC))
        .model(DataGenUtil::onlyInfo)
        .build()
        .register();

    public static final BlockEntry<MineralFountainPressurizerBlock> MINERAL_FOUNTAIN_PRESSURIZER = NekoPlus.REGISTRUM
        .block("mineral_fountail_pressurizer", MineralFountainPressurizerBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .loot((tab, block) -> tab.add(
            block,
            LootTable.lootTable()
                .withPool(
                    tab.applyExplosionCondition(
                        block,
                        LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(
                                LootItem.lootTableItem(block)
                                    .when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(
                                                MineralFountainPressurizerBlock.PART,
                                                Cube323PartHalf.BOTTOM_CENTER
                                            ))
                                    )
                            )
                    )
                )
        ))
        .item()
        .build()
        .register();

    public static ResourceKey<Recipe<?>> recipeLocation(String path) {
        return ResourceKey.create(Registries.RECIPE, NekoPlus.location(path));
    }


    public static void setupRegistration() {
    }
}

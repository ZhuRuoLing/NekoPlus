package icu.takeneko.nekoplus.all;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumRecipeProvider;
import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.init.item.ModItems;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.block.FusionReactorControllerBlock;
import icu.takeneko.nekoplus.block.NPHatchBlock;
import icu.takeneko.nekoplus.block.HighEnergyLaserBlock;
import icu.takeneko.nekoplus.block.ParticleStabilizerBlock;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.block.StellarEngineBlock;
import icu.takeneko.nekoplus.block.TardisBlock;
import icu.takeneko.nekoplus.block.TitaniumAlloyAnvilBlock;
import icu.takeneko.nekoplus.block.property.Part3;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.NPHatchTypes;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchType;
import icu.takeneko.nekoplus.util.ModelUtils;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.Tags;

public class NPBlocks {

    static {
        NekoPlus.REGISTRUM.defaultCreativeTab(NPCreativeTabs.TAB.getKey());
    }

    public static final BlockEntry<Block> ROYAL_STEEL_CASING = NekoPlus.REGISTRUM
        .block("royal_steel_casing", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p.strength(2f, 6.0F))
        .item()
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern(" A ")
                .pattern("A A")
                .pattern(" A ")
                .define('A', ModItems.ROYAL_STEEL_INGOT)
                .unlockedBy("has_" + prov.safeName(ModItems.ROYAL_STEEL_INGOT), RegistrumRecipeProvider.has(ModItems.ROYAL_STEEL_INGOT))
                .save(prov);
        })
        .build()
        .register();

    public static final BlockEntry<ParticleStabilizerBlock> PARTICLE_STABILIZER = NekoPlus.REGISTRUM
        .block("particle_stabilizer", ParticleStabilizerBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(BlockBehaviour.Properties::noOcclusion)
        .blockstate((ctx, cons) -> {
            cons.getVariantBuilder(ctx.get())
                .forAllStates(blockState -> {
                    boolean isOverload = blockState.getValue(ParticleStabilizerBlock.OVERLOAD);
                    boolean isCooling = blockState.getValue(ParticleStabilizerBlock.COOLING);
                    ResourceLocation texture;
                    if (isOverload) {
                        texture = NekoPlus.location("block/particle_stabilizer_overload");
                    } else {
                        if (isCooling) {
                            texture = NekoPlus.location("block/particle_stabilizer_freezing");
                        } else {
                            texture = NekoPlus.location("block/particle_stabilizer");
                        }
                    }
                    ModelFile file = cons.models()
                        .withExistingParent(texture.getPath().replaceFirst("block/", ""), NekoPlus.location("block/particle_stabilizer_parent"))
                        .texture("1", texture);
                    return ConfiguredModel.builder()
                        .modelFile(file)
                        .build();
                });
        })
        .item()
        .properties(p -> p.rarity(Rarity.UNCOMMON))
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("ADA")
                .pattern("BCB")
                .pattern("AEA")
                .define('A', ModItemTags.GEMS_TOPAZ)
                .define('B', ModBlocks.EMBER_GLASS)
                .define('C', ROYAL_STEEL_CASING)
                .define('D', NPItems.CRYOCOOLER)
                .define('E', ModItems.CIRCUIT_BOARD)
                .unlockedBy("has_" + ModItemTags.GEMS_TOPAZ.location().getPath(), RegistrumRecipeProvider.has(ModItemTags.GEMS_TOPAZ))
                .unlockedBy("has_" + ModBlocks.EMBER_GLASS.getRegisteredName(), RegistrumRecipeProvider.has(ModBlocks.EMBER_GLASS))
                .unlockedBy("has_" + ROYAL_STEEL_CASING.getRegisteredName(), RegistrumRecipeProvider.has(ROYAL_STEEL_CASING))
                .unlockedBy("has_" + NPItems.CRYOCOOLER.getRegisteredName(), RegistrumRecipeProvider.has(NPItems.CRYOCOOLER))
                .unlockedBy("has_" + ModItems.CIRCUIT_BOARD.getRegisteredName(), RegistrumRecipeProvider.has(ModItems.CIRCUIT_BOARD))
                .save(prov);
        })
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.withExistingParent(ctx.getName(), NekoPlus.location("block/particle_stabilizer_parent"))
                    .texture("1", NekoPlus.location("block/particle_stabilizer"))
            );
        })
        .build()
        .register();

    public static final BlockEntry<HighEnergyLaserBlock> HIGH_ENERGY_LASER = NekoPlus.REGISTRUM
        .block("high_energy_laser", HighEnergyLaserBlock::new)
        .blockstate((ctx, prov) -> {
            // "3": "nekoplus:block/high_energy_laser",
            ResourceLocation textureOff = NekoPlus.location("block/high_energy_laser_off");
            ResourceLocation textureOverload = NekoPlus.location("block/high_energy_laser_overload");
            ResourceLocation texture = NekoPlus.location("block/high_energy_laser");
            BlockModelBuilder modelOff = prov.models().withExistingParent(ctx.getName() + "_off", NekoPlus.location("block/high_energy_laser_base"))
                .texture("3", textureOff);
            BlockModelBuilder modelOverload = prov.models().withExistingParent(ctx.getName() + "_overload", NekoPlus.location("block/high_energy_laser_base"))
                .texture("3", textureOverload);
            BlockModelBuilder model = prov.models().withExistingParent(ctx.getName(), NekoPlus.location("block/high_energy_laser_base"))
                .texture("3", texture);

            prov.getVariantBuilder(ctx.get())
                .forAllStates(blockState -> {
                    boolean overload = blockState.getValue(HighEnergyLaserBlock.OVERLOAD);
                    boolean powered = blockState.getValue(HighEnergyLaserBlock.POWERED);
                    Direction facing = blockState.getValue(HighEnergyLaserBlock.FACING);
                    BlockModelBuilder m;
                    if (powered) {
                        m = modelOff;
                    } else {
                        if (overload) {
                            m = modelOverload;
                        } else {
                            m = model;
                        }
                    }
                    int yRot = facing.getAxis() != Direction.Axis.Y ? ((int) facing.toYRot() + 180) % 360 : 0;
                    int xRot = 0;
                    if (facing.getAxis() == Direction.Axis.Y) {
                        if (facing == Direction.DOWN) {
                            xRot = 180;
                        }
                    } else {
                        xRot = 90;
                    }
                    return ConfiguredModel.builder()
                        .modelFile(m)
                        .rotationY(yRot)
                        .rotationX(xRot)
                        .build();
                });
        })
        .item()
        .properties(p -> p.rarity(Rarity.RARE))
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ctx.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern("DED")
                .define('A', ModItems.TIN_NUGGET)
                .define('B', NPItems.CARBON_DIOXIDE_LASER_TUBE)
                .define('C', NPItems.TITANIUM_ALLOY_INGOT)
                .define('D', ModItems.TRANSCENDIUM_INGOT)
                .define('E', NPItems.INTEGRATED_CHIP_CIRCUIT_BOARD)
                .unlockedBy("has_" + ModItems.TIN_NUGGET.getRegisteredName(), RegistrumRecipeProvider.has(ModItems.TIN_NUGGET))
                .unlockedBy("has_" + NPItems.CARBON_DIOXIDE_LASER_TUBE.getRegisteredName(), RegistrumRecipeProvider.has(NPItems.CARBON_DIOXIDE_LASER_TUBE))
                .unlockedBy("has_" + NPItems.TITANIUM_ALLOY_INGOT.getRegisteredName(), RegistrumRecipeProvider.has(NPItems.TITANIUM_ALLOY_INGOT))
                .unlockedBy("has_" + NPItems.INTEGRATED_CHIP_CIRCUIT_BOARD.getRegisteredName(), RegistrumRecipeProvider.has(NPItems.INTEGRATED_CHIP_CIRCUIT_BOARD))
                .save(prov);
        })
        .build()
        .register();

    public static final BlockEntry<ProgrammableLogicGateBlock> PROGRAMMABLE_LOGIC_GATE = NekoPlus.REGISTRUM
        .block("programmable_logic_gate", ProgrammableLogicGateBlock::new)
        .properties(properties -> properties.strength(3.0F, 3.5F).sound(SoundType.STONE).noOcclusion())
        .blockstate((ctx, cons) -> {
            MultiPartBlockStateBuilder builder = cons.getMultipartBuilder(ctx.get());
            ModelFile base = cons.models().getExistingFile(NekoPlus.location("block/programmable_logic_gate"));
            ModelFile torchOn = cons.models().getExistingFile(NekoPlus.location("block/programmable_logic_gate_torch_on"));
            ModelFile torchOff = cons.models().getExistingFile(NekoPlus.location("block/programmable_logic_gate_torch_off"));
            Direction.Plane.HORIZONTAL.stream().forEach(it -> {
                int yRot = ((int) it.toYRot() + 180) % 360;
                builder.part()
                    .modelFile(base)
                    .rotationY(yRot)
                    .addModel()
                    .condition(ProgrammableLogicGateBlock.FACING, it)
                    .end();
                int yRotE = 0;
                for (BooleanProperty p : ProgrammableLogicGateBlock.PROPERTIES_ENABLE) {
                    builder.part()
                        .modelFile(torchOn)
                        .rotationY(yRot + yRotE)
                        .addModel()
                        .condition(ProgrammableLogicGateBlock.FACING, it)
                        .condition(p, true)
                        .end();

                    builder.part()
                        .modelFile(torchOff)
                        .rotationY(yRot + yRotE)
                        .addModel()
                        .condition(ProgrammableLogicGateBlock.FACING, it)
                        .condition(p, false)
                        .end();
                    yRotE += 90;
                }
            });
        })
        .item()
        .recipe((ctx, prov) ->
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ctx.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern("DDD")
                .define('A', Ingredient.of(Items.REDSTONE_TORCH.getDefaultInstance()))
                .define('B', ModBlocks.ADVANCED_COMPARATOR)
                .define('C', NPItems.ADVANCED_PROCESSOR)
                .define('D', Ingredient.of(Items.IRON_INGOT))
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(Items.REDSTONE_TORCH).getPath(), RegistrumRecipeProvider.has(Items.REDSTONE_TORCH))
                .unlockedBy("has_" + ModBlocks.ADVANCED_COMPARATOR.getRegisteredName(), RegistrumRecipeProvider.has(ModBlocks.ADVANCED_COMPARATOR))
                .unlockedBy("has_" + NPItems.ADVANCED_PROCESSOR.getRegisteredName(), RegistrumRecipeProvider.has(NPItems.ADVANCED_PROCESSOR))
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(Items.IRON_INGOT).getPath(), RegistrumRecipeProvider.has(Items.IRON_INGOT))
                .save(prov, prov.safeId(ctx.getId()))
        )
        .model((ctx, prov) -> {
        })
        .build()
        .register();

    public static final BlockEntry<StellarEngineBlock> STELLAR_ENGINE = NekoPlus.REGISTRUM
        .block("stellar_engine", StellarEngineBlock::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p.sound(SoundType.METAL)
            .noOcclusion()
            .emissiveRendering((state, level, pos) -> true)
        )
        .blockstate((ctx, cons) -> {
            cons.simpleBlock(
                ctx.get(),
                cons.models()
                    .getBuilder("block/stellar_engine")
                    .texture("particle", "nekoplus:block/stellar_engine")
            );
        })
        .item()
        .properties(p -> p.rarity(Rarity.EPIC))
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.getBuilder(ctx.getName())
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
            );
        })
        .build()
        .register();

    public static final BlockEntry<TardisBlock> TARDIS = NekoPlus.REGISTRUM
        .block("tardis", TardisBlock::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p.sound(SoundType.METAL)
            .noOcclusion()
            .explosionResistance(114514)
        )
        .blockstate((ctx, cons) -> {
            cons.simpleBlock(
                ctx.get(),
                cons.models()
                    .getBuilder("block/tardis")
                    .texture("particle", "nekoplus:block/tardis")
            );
        })
        .loot((tab, block) -> {
            tab.add(
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
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TardisBlock.PART, Part3.BOTTOM))
                                        )
                                )
                        )
                    )
            );
        })
        .item()
        .properties(p -> p.rarity(Rarity.EPIC))
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.getBuilder(ctx.getName())
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
            );
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

//    public static final BlockEntry<Block> SILICON_BLOCK = NekoPlus.REGISTRUM
//        .block("silicon_block", Block::new)
//        .initialProperties(() -> Blocks.IRON_BLOCK)
//        .tag(Tags.Blocks.STORAGE_BLOCKS, NPTags.Blocks.STORAGE_BLOCKS_SILICON)
//        .item()
//        .tag(Tags.Items.STORAGE_BLOCKS, NPTags.Items.STORAGE_BLOCKS_SILICON)
//        .build()
//        .register();

    public static final BlockEntry<Block> TITANIUM_ALLOY_BLOCK = NekoPlus.REGISTRUM
        .block("titanium_alloy_block", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .tag(Tags.Blocks.STORAGE_BLOCKS, NPTags.Blocks.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .item()
        .tag(Tags.Items.STORAGE_BLOCKS, NPTags.Items.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .build()
        .register();

    public static final BlockEntry<TitaniumAlloyAnvilBlock> TITANIUM_ALLOY_ANVIL = NekoPlus.REGISTRUM
        .block("titanium_alloy_anvil", TitaniumAlloyAnvilBlock::new)
        .initialProperties(() -> Blocks.ANVIL)
        .properties(p -> p.pushReaction(PushReaction.NORMAL))
        .tag(ModBlockTags.ANVIL_TIER_0, ModBlockTags.ANVIL_TIER_1, BlockTags.ANVIL, ModBlockTags.CANT_BROKEN_ANVIL, BlockTags.MINEABLE_WITH_PICKAXE)
        .properties(p -> p.isValidSpawn(Blocks::never).strength(5.0f, 1200f))
        .blockstate((ctx, prov) -> {
            ModelFile modelFile = prov.models().getExistingFile(NekoPlus.location("block/titanium_alloy_anvil"));
            prov.getVariantBuilder(ctx.get())
                .forAllStates(blockState -> {
                    Direction value = blockState.getValue(AnvilBlock.FACING);
                    int yRot = ((int) value.toYRot()) % 360;
                    return ConfiguredModel.builder()
                        .rotationY(yRot)
                        .modelFile(modelFile)
                        .build();
                });
        })
        .item()
        .tag(ItemTags.ANVIL)
        .recipe((src, ctx) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, src.get())
                .pattern("AAA")
                .pattern(" B ")
                .pattern("BBB")
                .define('A', TITANIUM_ALLOY_BLOCK)
                .define('B', NPItems.TITANIUM_ALLOY_INGOT)
                .unlockedBy("has_" + ctx.safeName(TITANIUM_ALLOY_BLOCK), RegistrumRecipeProvider.has(TITANIUM_ALLOY_BLOCK))
                .unlockedBy("has_" + ctx.safeName(NPItems.TITANIUM_ALLOY_INGOT), RegistrumRecipeProvider.has(NPItems.TITANIUM_ALLOY_INGOT))
                .save(ctx);
        })
        .build()
        .register();

    public static final BlockEntry<FusionReactorControllerBlock> FUSION_REACTOR_CONTROLLER = NekoPlus.REGISTRUM
        .block("fusion_reactor_controller", FusionReactorControllerBlock::new)
        .lang("Fusion Reactor Integrated Controller MK1")
        .blockstate((ctx, prov) -> {
            ModelFile modelFile = prov.models()
                .withExistingParent(ctx.getName(), NekoPlus.location("block/hatch_base"))
                .texture("all", NekoPlus.location("block/royal_steel_casing"))
                .texture("overlay", NekoPlus.location("block/laser_confinement_fusion"));

            prov.getVariantBuilder(ctx.get())
                .forAllStates(blockState -> {
                    Direction facing = blockState.getValue(FusionReactorControllerBlock.FACING);
                    int yRot = facing.getAxis() != Direction.Axis.Y ? ((int) facing.toYRot() + 180) % 360 : 0;
                    int xRot = 0;
                    if (facing.getAxis() == Direction.Axis.Y) {
                        if (facing == Direction.DOWN) {
                            xRot = 180;
                        }
                    } else {
                        xRot = 90;
                    }
                    return ConfiguredModel
                        .builder()
                        .rotationX(xRot)
                        .rotationY(yRot)
                        .uvLock(true)
                        .modelFile(modelFile)
                        .build();
                });
        })
        .item()
        .properties(p -> p.rarity(Rarity.EPIC))
        .recipe((ctx, prov) -> {
        })
        .build()
        .register();

    public static final BlockEntry<NPHatchBlock> ITEM_INPUT_HATCH = hatch(NPHatchTypes.ITEM, true, ModBlocks.CHUTE);

    public static final BlockEntry<NPHatchBlock> ITEM_OUTPUT_HATCH = hatch(NPHatchTypes.ITEM, false, ModBlocks.CHUTE);

    public static final BlockEntry<NPHatchBlock> ENERGY_OUTPUT_HATCH = hatch(NPHatchTypes.ENERGY, false, ModItems.SUPER_CAPACITOR_EMPTY);

    public static BlockEntry<NPHatchBlock> hatch(HatchType<?> type, boolean isInput, ItemLike recipeItem) {
        String id = type.getSerializedName() + (isInput ? "_input" : "_output") + "_hatch";
        return NekoPlus.REGISTRUM
            .block(id, p -> new NPHatchBlock(p, type, isInput))
            .blockstate((ctx, prov) -> {
                ModelFile modelFile = prov.models()
                    .withExistingParent(ctx.getName(), NekoPlus.location("block/hatch_base"))
                    .texture("all", NekoPlus.location("block/royal_steel_casing"))
                    .texture("overlay", NekoPlus.location("block/" + "hatch_" + type.getSerializedName() + (isInput ? "_input" : "_output")));

                prov.getVariantBuilder(ctx.get())
                    .forAllStates(blockState -> {
                        Direction facing = blockState.getValue(NPHatchBlock.FACING);
                        int yRot = facing.getAxis() != Direction.Axis.Y ? ((int) facing.toYRot() + 180) % 360 : 0;
                        int xRot = 0;
                        if (facing.getAxis() == Direction.Axis.Y) {
                            if (facing == Direction.DOWN) {
                                xRot = 180;
                            }
                        } else {
                            xRot = 90;
                        }
                        return ConfiguredModel
                            .builder()
                            .rotationX(xRot)
                            .rotationY(yRot)
                            .uvLock(true)
                            .modelFile(modelFile)
                            .build();
                    });
            })
            .item()
            .recipe((ctx, prov) -> {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ctx.get())
                    .requires(recipeItem)
                    .requires(ROYAL_STEEL_CASING)
                    .unlockedBy("has_" + prov.safeName(recipeItem), RegistrumRecipeProvider.has(recipeItem))
                    .unlockedBy("has_" + prov.safeName(ROYAL_STEEL_CASING), RegistrumRecipeProvider.has(ROYAL_STEEL_CASING))
                    .save(prov);
            })
            .build()
            .register();
    }

    public static void setupRegistration() {
    }
}

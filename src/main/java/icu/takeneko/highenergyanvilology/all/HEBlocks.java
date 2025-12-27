package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.init.item.ModItems;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.block.AnvilonEmitterBlock;
import icu.takeneko.highenergyanvilology.block.HighEnergyLaserBlock;
import icu.takeneko.highenergyanvilology.block.ParticleStabilizerBlock;
import icu.takeneko.highenergyanvilology.block.StellarEngineBlock;
import icu.takeneko.highenergyanvilology.block.TardisBlock;
import icu.takeneko.highenergyanvilology.block.property.Part3;
import icu.takeneko.highenergyanvilology.util.ModelUtils;
import icu.takeneko.highenergyanvilology.util.StateUtils;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;

public class HEBlocks {

    static {
        HEAnvilology.REGISTRATE.defaultCreativeTab(HECreativeTabs.TAB.getKey());
    }

    public static final BlockEntry<AnvilonEmitterBlock> ANVILON_EMITTER_BLOCK = HEAnvilology.REGISTRATE
        .block("anvilon_emitter", AnvilonEmitterBlock::new)
        .properties(prop -> Blocks.IRON_BLOCK.properties()
            .noOcclusion()
            .isRedstoneConductor(StateUtils::always)
            .isSuffocating(StateUtils::never)
            .isViewBlocking(StateUtils::never)
        )
        .tag(ModBlockTags.LASER_CAN_PASS_THROUGH)
        .defaultBlockstate()
        .blockstate((ctx, cons) -> {
            cons.simpleBlock(
                ctx.get(),
                cons.models()
                    .getBuilder("block/anvilon_emitter")
                    .texture("particle", "highenergyanvilology:block/anvilon_emitter")
            );
        })
        .defaultLang()
        .defaultLoot()
        .item()
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.getBuilder(ctx.getName())
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
            );
        })
        .build()
        .register();

    public static final BlockEntry<Block> ROYAL_STEEL_CASING = HEAnvilology.REGISTRATE
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
                .unlockedBy("has_" + prov.safeName(ModItems.ROYAL_STEEL_INGOT), RegistrateRecipeProvider.has(ModItems.ROYAL_STEEL_INGOT))
                .save(prov);
        })
        .build()
        .register();

    public static final BlockEntry<ParticleStabilizerBlock> PARTICLE_STABILIZER = HEAnvilology.REGISTRATE
        .block("particle_stabilizer", ParticleStabilizerBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .blockstate((ctx, cons) -> {
            cons.simpleBlock(
                ctx.get(),
                cons.models()
                    .getBuilder("block/particle_stabilizer")
                    .texture("particle", "highenergyanvilology:block/particle_stabilizer")
            );
        })
        .item()
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("ADA")
                .pattern("BCB")
                .pattern("AEA")
                .define('A', ModItemTags.GEMS_TOPAZ)
                .define('B', ModBlocks.EMBER_GLASS)
                .define('C', ROYAL_STEEL_CASING)
                .define('D', HEItems.CRYOCOOLER)
                .define('E', ModItems.CIRCUIT_BOARD);
        })
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.getBuilder(ctx.getName())
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
            );
        })
        .build()
        .register();

    public static final BlockEntry<HighEnergyLaserBlock> HIGH_ENERGY_LASER = HEAnvilology.REGISTRATE
        .block("high_energy_laser", HighEnergyLaserBlock::new)
        .blockstate((ctx, prov) -> {
            // "3": "highenergyanvilology:block/high_energy_laser",
            ResourceLocation textureOff = HEAnvilology.location("block/high_energy_laser_off");
            ResourceLocation textureOverload = HEAnvilology.location("block/high_energy_laser_overload");
            ResourceLocation texture = HEAnvilology.location("block/high_energy_laser");
            BlockModelBuilder modelOff = prov.models().withExistingParent(ctx.getName() + "_off", HEAnvilology.location("block/high_energy_laser_base"))
                .texture("3", textureOff);
            BlockModelBuilder modelOverload = prov.models().withExistingParent(ctx.getName() + "_overload", HEAnvilology.location("block/high_energy_laser_base"))
                .texture("3", textureOverload);
            BlockModelBuilder model = prov.models().withExistingParent(ctx.getName(), HEAnvilology.location("block/high_energy_laser_base"))
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
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ctx.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern("DED")
                .define('A', ModItems.TIN_NUGGET)
                .define('B', HEItems.CARBON_DIOXIDE_LASER_TUBE)
                .define('C', HEItems.TITANIUM_ALLOY_INGOT)
                .define('D', ModItems.TRANSCENDIUM_INGOT)
                .define('E', ModItems.CIRCUIT_BOARD)
                .unlockedBy("has_" + ModItems.TIN_NUGGET.getRegisteredName(), RegistrateRecipeProvider.has(ModItems.TIN_NUGGET))
                .unlockedBy("has_" + HEItems.CARBON_DIOXIDE_LASER_TUBE.getRegisteredName(), RegistrateRecipeProvider.has(HEItems.CARBON_DIOXIDE_LASER_TUBE))
                .unlockedBy("has_" + HEItems.TITANIUM_ALLOY_INGOT.getRegisteredName(), RegistrateRecipeProvider.has(HEItems.TITANIUM_ALLOY_INGOT))
                .unlockedBy("has_" + ModItems.CIRCUIT_BOARD.getRegisteredName(), RegistrateRecipeProvider.has(ModItems.CIRCUIT_BOARD))
                .save(prov);
        })
        .build()
        .register();

    public static final BlockEntry<StellarEngineBlock> STELLAR_ENGINE = HEAnvilology.REGISTRATE
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
                    .texture("particle", "highenergyanvilology:block/stellar_engine")
            );
        })
        .item()
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.getBuilder(ctx.getName())
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
            );
        })
        .build()
        .register();

    public static final BlockEntry<TardisBlock> TARDIS = HEAnvilology.REGISTRATE
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
                    .texture("particle", "highenergyanvilology:block/tardis")
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
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.getBuilder(ctx.getName())
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
            );
        })
        .build()
        .register();

    public static final BlockEntry<Block> TITANIUM_ALLOY_BLOCK = HEAnvilology.REGISTRATE
        .block("titanium_alloy_block", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .tag(Tags.Blocks.STORAGE_BLOCKS, HETags.Blocks.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .item()
        .tag(Tags.Items.STORAGE_BLOCKS, HETags.Items.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .build()
        .register();

    public static void setupRegistration() {
    }
}

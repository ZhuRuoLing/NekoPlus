package icu.takeneko.nekoplus.all;

import dev.anvilcraft.lib.v2.registrum.util.entry.ItemEntry;
import dev.anvilcraft.lib.v2.util.predicate.ChanceItemStack;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.ChargerChargingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.CookingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.StampingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.SuperHeatingRecipe;
import dev.dubhe.anvilcraft.util.registrater.DataGenUtil;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.item.module.impl.AntiGravityModule;
import icu.takeneko.nekoplus.foundation.item.module.impl.ExoskeletalLegFrameModule;
import icu.takeneko.nekoplus.foundation.item.module.impl.MechanicalHeartModule;
import icu.takeneko.nekoplus.foundation.item.module.impl.TitaniumCrystalModule;
import icu.takeneko.nekoplus.item.EnhancementModuleItem;
import icu.takeneko.nekoplus.item.ModularSmithingTemplate;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.recipe.LaserEtchingRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class NPItems {
    static {
        NekoPlus.REGISTRUM.defaultCreativeTab(NPCreativeTabs.TAB.getKey());
    }

    public static final ItemEntry<Item> TITANIUM_ALLOY_INGOT = NekoPlus.REGISTRUM
        .item("titanium_alloy_ingot", Item::new)
        .recipe((c, p) -> {
            SuperHeatingRecipe.builder()
                .requires(p.getItems(), ModItemTags.TITANIUM_INGOTS)
                .requires(Items.IRON_INGOT)
                .result(c.get(), 2)
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_superheating")));
            p.storage(c, RecipeCategory.MISC, NPBlocks.TITANIUM_ALLOY_BLOCK);
        })
        .register();

    public static final ItemEntry<Item> TITANIUM_ALLOY_NUGGET = NekoPlus.REGISTRUM
        .item("titanium_alloy_nugget", Item::new)
        .recipe((c, p) ->
            p.storage(c, RecipeCategory.MISC, TITANIUM_ALLOY_INGOT)
        )
        .register();

    public static final ItemEntry<Item> ADVANCED_PROCESSOR = NekoPlus.REGISTRUM
        .item("advanced_processor", Item::new)
        .recipe((ctx, prov) ->
            LaserEtchingRecipe.builder()
                .input(Ingredient.of(ModItems.PROCESSOR))
                .output(ChanceItemStack.of(new ItemStackTemplate(ctx.get()), 1))
                .build()
                .save(prov, ctx.getId())
        )
        .register();

    public static final ItemEntry<Item> INTEGRATED_CHIP_CIRCUIT_BOARD = NekoPlus.REGISTRUM
        .item("integrated_chip_circuit_board", Item::new)
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(p.getItems(), RecipeCategory.MISC, c.get())
                .pattern("ABB")
                .pattern("ACB")
                .pattern("DDD")
                .define('A', NPTags.Items.SILVER_PLATE)
                .define('B', ModItems.CIRCUIT_BOARD)
                .define('C', ADVANCED_PROCESSOR)
                .define('D', ModItems.HARDEND_RESIN)
                .unlockedBy("has_silver_plate", p.has(NPTags.Items.SILVER_PLATE))
                .unlockedBy(
                    "has_" + p.safeName(ModItems.CIRCUIT_BOARD),
                    p.has(ModItems.CIRCUIT_BOARD)
                )
                .unlockedBy(
                    "has_" + ADVANCED_PROCESSOR.getRegisteredName(),
                    p.has(ADVANCED_PROCESSOR)
                )
                .unlockedBy(
                    "has_" + ModItems.HARDEND_RESIN.getRegisteredName(),
                    p.has(ModItems.HARDEND_RESIN)
                )
                .save(p);
        })
        .register();

    public static final ItemEntry<ModularSmithingTemplate> MODULAR_ENHANCEMENT_TEMPLATE = NekoPlus.REGISTRUM
        .item("modular_enhancement_template", ModularSmithingTemplate::new)
        .tag(ModItemTags.TEMPLATES)
        .register();

    public static final ItemEntry<Item> ENHANCEMENT_MODULE_BASE = NekoPlus.REGISTRUM
        .item("enhancement_module_base", Item::new)
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.TOOLS, ctx.get(), 16)
                .pattern("DC ")
                .pattern("BAB")
                .define('A', INTEGRATED_CHIP_CIRCUIT_BOARD)
                .define('B', Items.COPPER_INGOT)
                .define('C', ModBlocks.POWER_CONVERTER_SMALL)
                .define('D', ModItems.CAPACITOR_EMPTY)
                .unlockedBy(
                    "has_" + INTEGRATED_CHIP_CIRCUIT_BOARD.getRegisteredName(),
                    prov.has(INTEGRATED_CHIP_CIRCUIT_BOARD)
                )
                .unlockedBy(
                    "has_" + prov.safeName(Items.COPPER_INGOT),
                    prov.has(Items.COPPER_INGOT)
                )
                .unlockedBy(
                    "has_" + ModBlocks.POWER_CONVERTER_SMALL.getRegisteredName(),
                    prov.has(ModBlocks.POWER_CONVERTER_SMALL)
                )
                .unlockedBy(
                    "has_" + ModItems.CAPACITOR_EMPTY.getRegisteredName(),
                    prov.has(ModItems.CAPACITOR_EMPTY)
                ).save(prov);
        })
        .register();

    public static final ItemEntry<EnhancementModuleItem<AntiGravityModule>> ANTI_GRAVITY_MODULE = NekoPlus.REGISTRUM
        .item("anti_gravity_module", p -> new EnhancementModuleItem<>(p, AntiGravityModule.TYPE))
        .register();

    public static final ItemEntry<EnhancementModuleItem<TitaniumCrystalModule>> TITANIUM_CRYSTAL_MODULE = NekoPlus.REGISTRUM
        .item("titanium_crystal_module", p -> new EnhancementModuleItem<>(p, TitaniumCrystalModule.TYPE))
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.TOOLS, ctx.get(), 16)
                .pattern("BBB")
                .pattern("B B")
                .pattern("BAB")
                .define('A', ENHANCEMENT_MODULE_BASE)
                .define('B', NPBlocks.TITANIUM_ALLOY_BLOCK)
                .unlockedBy(
                    "has_" + ENHANCEMENT_MODULE_BASE.getRegisteredName(),
                    prov.has(ENHANCEMENT_MODULE_BASE)
                )
                .unlockedBy(
                    "has_" + NPBlocks.TITANIUM_ALLOY_BLOCK.getRegisteredName(),
                    prov.has(NPBlocks.TITANIUM_ALLOY_BLOCK)
                ).save(prov);
        })
        .register();

    public static final ItemEntry<EnhancementModuleItem<MechanicalHeartModule>> MECHANICAL_HEART_MODULE = NekoPlus.REGISTRUM
        .item("mechanical_heart_module", p -> new EnhancementModuleItem<>(p, MechanicalHeartModule.TYPE))
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.TOOLS, ctx.get(), 16)
                .pattern("BCB")
                .pattern("DED")
                .pattern("BAB")
                .define('A', ENHANCEMENT_MODULE_BASE)
                .define('B', ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK)
                .define('C', ModBlocks.MAGNETIC_CHUTE)
                .define('D', ModItems.CAPACITOR)
                .define('E', ModBlocks.FLUID_TANK)
                .unlockedBy(
                    "has_" + ENHANCEMENT_MODULE_BASE.getRegisteredName(),
                    prov.has(ENHANCEMENT_MODULE_BASE)
                )
                .unlockedBy(
                    "has_" + ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK.getRegisteredName(),
                    prov.has(ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK)
                )
                .unlockedBy(
                    "has_" + ModBlocks.MAGNETIC_CHUTE.getRegisteredName(),
                    prov.has(ModBlocks.MAGNETIC_CHUTE)
                )
                .unlockedBy(
                    "has_" + ModItems.CAPACITOR.getRegisteredName(),
                    prov.has(ModItems.CAPACITOR)
                )
                .unlockedBy(
                    "has_" + ModBlocks.FLUID_TANK.getRegisteredName(),
                    prov.has(ModBlocks.FLUID_TANK)
                )
                .save(prov);
        })
        .register();

    public static final ItemEntry<EnhancementModuleItem<ExoskeletalLegFrameModule>> EXOSKELETAL_LEG_FRAME_MODULE = NekoPlus.REGISTRUM
        .item("exoskeletal_leg_frame_module", p -> new EnhancementModuleItem<>(p, ExoskeletalLegFrameModule.TYPE))
        .register();

    public static final ItemEntry<Item> DRY_ICE = NekoPlus.REGISTRUM
        .item("dry_ice", Item::new)
        .tag(NPTags.Items.DRY_ICES)
        .register();

    public static final ItemEntry<Item> SULFUR = NekoPlus.REGISTRUM
        .item("sulfur", Item::new)
        .tag(Tags.Items.DUSTS, NPTags.Items.SULFUR)
        .recipe((ctx, prov) -> {
            AirCondensingRecipe.builder()
                .dimension(prov.resolve(BuiltinDimensionTypes.NETHER))
                .results(List.of(new ItemStackTemplate(ctx.get(), 8), new ItemStackTemplate(DRY_ICE.asItem(), 1)))
                .probability(ConstantValue.exactly(0.3f))
                .ticks(10)
                .build()
                .save(prov, NekoPlus.location("air_condensing/nether"));
        })
        .register();

    public static final ItemEntry<Item> CHARGED_LEVITATION_POWDER = NekoPlus.REGISTRUM
        .item("charged_levitation_powder", Item::new)
        .recipe((c, p) -> {
            ChargerChargingRecipe.builder(p.getItems())
                .requires(ModItems.LEVITATION_POWDER)
                .result(c.get())
                .power(-8)
                .time(20)
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_charging")));
        })
        .register();

    public static final ItemEntry<Item> STABILIZE_POWDER = NekoPlus.REGISTRUM
        .item("stabilize_powder", Item::new)
        .tag(Tags.Items.DUSTS)
        .recipe((c, p) -> {
            SuperHeatingRecipe.builder()
                .requires(p.getItems(), NPTags.Items.SULFUR, 8)
                .requires(Items.LAPIS_LAZULI, 2)
                .requires(Items.REDSTONE, 3)
                .requires(NPItems.CHARGED_LEVITATION_POWDER, 5)
                .result(c.get(), 18)
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_superheating")));
        })
        .register();

    public static final ItemEntry<Item> CARBON_DIOXIDE_LASER_TUBE = NekoPlus.REGISTRUM
        .item("carbon_dioxide_laser_tube", Item::new)
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(prov.getItems(), RecipeCategory.MISC, ctx.get())
                .pattern(" A ")
                .pattern("CBC")
                .define('A', DRY_ICE)
                .define('B', Items.GLASS_BOTTLE)
                .define('C', ModItems.SILVER_NUGGET)
                .unlockedBy("has_" + DRY_ICE.getRegisteredName(), prov.has(DRY_ICE))
                .unlockedBy("has_" + prov.safeName(Items.GLASS_BOTTLE), prov.has(Items.GLASS_BOTTLE))
                .unlockedBy(
                    "has_" + ModItems.SILVER_NUGGET.getRegisteredName(),
                    prov.has(ModItems.SILVER_NUGGET)
                )
                .save(prov);
        })
        .register();

    public static final ItemEntry<Item> NANOFILTRATION_MEMBRANE = NekoPlus.REGISTRUM
        .item("nanofiltration_membrane", Item::new)
        .recipe((c, p) -> {
            StampingRecipe.builder()
                .requires(ModItems.HARDEND_RESIN, 4)
                .result(c.get())
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_stamping")));
        })
        .register();

    public static final ItemEntry<Item> AIR_FILTER = NekoPlus.REGISTRUM
        .item("air_filter", Item::new)
        .model(DataGenUtil::onlyInfo)
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(p.getItems(), RecipeCategory.MISC, c.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NPItems.TITANIUM_ALLOY_NUGGET)
                .define('B', NANOFILTRATION_MEMBRANE)
                .unlockedBy(
                    "has_" + p.safeName(NANOFILTRATION_MEMBRANE),
                    p.has(NANOFILTRATION_MEMBRANE)
                )
                .unlockedBy(
                    "has_" + p.safeName(TITANIUM_ALLOY_NUGGET),
                    p.has(TITANIUM_ALLOY_NUGGET)
                )
                .save(p);
        })
        .register();

    public static final ItemEntry<Item> CRYOCOOLER = NekoPlus.REGISTRUM
        .item("cryocooler", Item::new)
        .model(DataGenUtil::onlyInfo)
        .recipe((c, p) -> ShapedRecipeBuilder.shaped(p.getItems(), RecipeCategory.MISC, c.get())
            .pattern("CDC")
            .pattern("ABA")
            .pattern(" C ")
            .define('A', Tags.Items.STORAGE_BLOCKS_COPPER)
            .define('B', Blocks.BLUE_ICE)
            .define('C', ModItemTags.GEMS_SAPPHIRE)
            .define('D', ModItems.CIRCUIT_BOARD)
            .unlockedBy("has_gem_sapphire", p.has(ModItemTags.GEMS_SAPPHIRE))
            .save(p))
        .register();

    public static final ItemEntry<Item> GUMMY_BEAR = NekoPlus.REGISTRUM
        .item("gummy_bear", Item::new)
        .properties(p -> p.food(
            new FoodProperties.Builder()
                .alwaysEdible()
                .nutrition(2)
                .saturationModifier(2)
                .build()
        ))
        .model(DataGenUtil::onlyInfo)
        .recipe((ctx, prov) -> {
            CookingRecipe.builder()
                .requires(ModItems.RESIN, 2)
                .requires(Items.SUGAR, 2)
                .requires(Items.ORANGE_DYE, 1)
                .result(ctx.get(), 5)
                .save(prov, NekoPlus.location(ctx.getName()));
        })
        .register();

    public static void setupRegistration() {
    }
}

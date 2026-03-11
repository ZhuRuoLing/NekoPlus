package icu.takeneko.nekoplus.all;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.anvilcraft.lib.recipe.component.ChanceItemStack;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.ChargerChargingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.CookingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.StampingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.SuperHeatingRecipe;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.item.AnvilonEmissionTubeItem;
import icu.takeneko.nekoplus.item.MageneticConfinementVesselItem;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.recipe.LaserEtchingRecipe;
import icu.takeneko.nekoplus.util.DataGenUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class NPItems {
    static {
        NekoPlus.REGISTRATE.defaultCreativeTab(NPCreativeTabs.TAB.getKey());
    }

    public static final ItemEntry<Item> STRONG_MAGNET = NekoPlus.REGISTRATE
        .item("strong_magnet", Item::new)
        .recipe((c, p) ->
            ChargerChargingRecipe.builder()
                .requires(ModItems.EARTH_CORE_SHARD)
                .result(c.get())
                .power(-64)
                .time(20 * 2)
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_charging")))
        )
        .register();


    public static final ItemEntry<Item> TITANIUM_ALLOY_INGOT = NekoPlus.REGISTRATE
        .item("titanium_alloy_ingot", Item::new)
        .recipe((c, p) -> {
            SuperHeatingRecipe.builder()
                .requires(ModItemTags.TITANIUM_INGOTS)
                .requires(Items.IRON_INGOT)
                .result(c.get(), 2)
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_superheating")));
            p.storage(c, RecipeCategory.MISC, NPBlocks.TITANIUM_ALLOY_BLOCK);
        })
        .register();

    public static final ItemEntry<Item> TITANIUM_ALLOY_NUGGET = NekoPlus.REGISTRATE
        .item("titanium_alloy_nugget", Item::new)
        .recipe((c, p) ->
            p.storage(c, RecipeCategory.MISC, TITANIUM_ALLOY_INGOT)
        )
        .register();

    public static final ItemEntry<Item> SILICON_INGOT = NekoPlus.REGISTRATE
        .item("silicon_ingot", Item::new)
        .tag(NPTags.Items.SILICON)
        .recipe((c, p) -> {
            p.storage(c, RecipeCategory.MISC, NPBlocks.SILICON_BLOCK);

            SuperHeatingRecipe.builder()
                .requires(Items.QUARTZ, 4)
                .result(c.get().getDefaultInstance().copyWithCount(1))
                .save(p, NekoPlus.location("super_heating/silicon_ingot_from_quartz"));

            SuperHeatingRecipe.builder()
                .requires(Items.GLASS, 48)
                .requires(NPItems.STABILIZE_POWDER, 1)
                .result(c.get().getDefaultInstance().copyWithCount(1))
                .save(p, NekoPlus.location("super_heating/silicon_ingot_from_glass"));
        })
        .register();

    public static final ItemEntry<Item> SILICON_WAFER = NekoPlus.REGISTRATE
        .item("silicon_wafer", Item::new)
        .register();

    public static final ItemEntry<Item> SILICON_CHIP = NekoPlus.REGISTRATE
        .item("silicon_chip", Item::new)
        .recipe((ctx, prov) ->
            LaserEtchingRecipe.builder()
                .input(Ingredient.of(SILICON_WAFER))
                .output(ChanceItemStack.of(ctx.get().getDefaultInstance(), 4))
                .build()
                .save(ctx.getId(), prov)
        )
        .register();

    public static final ItemEntry<Item> INTEGRATED_CHIP_CIRCUIT_BOARD = NekoPlus.REGISTRATE
        .item("integrated_chip_circuit_board", Item::new)
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                .pattern("ABB")
                .pattern("ACB")
                .pattern("DDD")
                .define('A', NPTags.Items.SILVER_PLATE)
                .define('B', ModItems.CIRCUIT_BOARD)
                .define('C', SILICON_CHIP)
                .define('D', ModItems.HARDEND_RESIN)
                .unlockedBy("has_silver_plate", RegistrateRecipeProvider.has(NPTags.Items.SILVER_PLATE))
                .unlockedBy("has_" + p.safeName(ModItems.CIRCUIT_BOARD), RegistrateRecipeProvider.has(ModItems.CIRCUIT_BOARD))
                .unlockedBy("has_" + SILICON_CHIP.getRegisteredName(), RegistrateRecipeProvider.has(SILICON_CHIP))
                .unlockedBy("has_" + ModItems.HARDEND_RESIN.getRegisteredName(), RegistrateRecipeProvider.has(ModItems.HARDEND_RESIN))
                .save(p);
        })
        .register();

    public static final ItemEntry<Item> DRY_ICE = NekoPlus.REGISTRATE
        .item("dry_ice", Item::new)
        .tag(NPTags.Items.DRY_ICES)
        .register();

    public static final ItemEntry<Item> SULFUR = NekoPlus.REGISTRATE
        .item("sulfur", Item::new)
        .tag(Tags.Items.DUSTS, NPTags.Items.SULFUR)
        .recipe((ctx, prov) -> {
            AirCondensingRecipe.builder()
                .dimension(prov.resolve(BuiltinDimensionTypes.NETHER))
                .results(List.of(new ItemStack(ctx.get(), 8), new ItemStack(DRY_ICE.asItem(), 1)))
                .probability(ConstantValue.exactly(0.3f))
                .ticks(10)
                .build()
                .save(NekoPlus.location("air_condensing/nether"), prov);
        })
        .register();

    public static final ItemEntry<Item> CHARGED_LEVITATION_POWDER = NekoPlus.REGISTRATE
        .item("charged_levitation_powder", Item::new)
        .recipe((c, p) -> {
            ChargerChargingRecipe.builder()
                .requires(ModItems.LEVITATION_POWDER)
                .result(c.get())
                .power(-8)
                .time(20)
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_charging")));
        })
        .register();

    public static final ItemEntry<Item> STABILIZE_POWDER = NekoPlus.REGISTRATE
        .item("stabilize_powder", Item::new)
        .tag(Tags.Items.DUSTS)
        .recipe((c, p) -> {
            SuperHeatingRecipe.builder()
                .requires(NPTags.Items.SULFUR, 8)
                .requires(Items.LAPIS_LAZULI, 2)
                .requires(Items.REDSTONE, 3)
                .requires(NPItems.CHARGED_LEVITATION_POWDER, 5)
                .result(c.get(), 18)
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_superheating")));
        })
        .register();

    public static final ItemEntry<Item> CARBON_DIOXIDE_LASER_TUBE = NekoPlus.REGISTRATE
        .item("carbon_dioxide_laser_tube", Item::new)
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern(" A ")
                .pattern("CBC")
                .define('A', DRY_ICE)
                .define('B', Items.GLASS_BOTTLE)
                .define('C', ModItems.SILVER_NUGGET)
                .unlockedBy("has_" + DRY_ICE.getRegisteredName(), RegistrateRecipeProvider.has(DRY_ICE))
                .unlockedBy("has_" + prov.safeName(Items.GLASS_BOTTLE), RegistrateRecipeProvider.has(Items.GLASS_BOTTLE))
                .unlockedBy("has_" + ModItems.SILVER_NUGGET.getRegisteredName(), RegistrateRecipeProvider.has(ModItems.SILVER_NUGGET))
                .save(prov);
        })
        .register();

    public static final ItemEntry<Item> NANOFILTRATION_MEMBRANE = NekoPlus.REGISTRATE
        .item("nanofiltration_membrane", Item::new)
        .recipe((c, p) -> {
            StampingRecipe.builder()
                .requires(ModItems.HARDEND_RESIN, 4)
                .result(c.get())
                .save(p, p.safeId(NekoPlus.location(c.getName() + "_stamping")));
        })
        .register();

    public static final ItemEntry<Item> AIR_FILTER = NekoPlus.REGISTRATE
        .item("air_filter", Item::new)
        .model(DataGenUtils::emptyConsumer)
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NPItems.TITANIUM_ALLOY_NUGGET)
                .define('B', NANOFILTRATION_MEMBRANE)
                .unlockedBy("has_" + p.safeName(NANOFILTRATION_MEMBRANE), RegistrateRecipeProvider.has(NANOFILTRATION_MEMBRANE))
                .unlockedBy("has_" + p.safeName(TITANIUM_ALLOY_NUGGET), RegistrateRecipeProvider.has(TITANIUM_ALLOY_NUGGET))
                .save(p);
        })
        .register();

    public static final ItemEntry<Item> CRYOCOOLER = NekoPlus.REGISTRATE
        .item("cryocooler", Item::new)
        .model(DataGenUtils::emptyConsumer)
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                .pattern("CDC")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', Tags.Items.STORAGE_BLOCKS_COPPER)
                .define('B', Blocks.BLUE_ICE)
                .define('C', ModItemTags.GEMS_SAPPHIRE)
                .define('D', ModItems.CIRCUIT_BOARD)
                .unlockedBy("has_gem_sapphire", RegistrateRecipeProvider.has(ModItemTags.GEMS_SAPPHIRE))
                .save(p);
        })
        .register();

    public static final ItemEntry<MageneticConfinementVesselItem> MAGNETIC_CONFINEMENT_VESSEL = NekoPlus.REGISTRATE
        .item("magnetic_confinement_vessel", MageneticConfinementVesselItem::new)
        .properties(p -> p.rarity(Rarity.RARE))
        .model(DataGenUtils::customRenderer)
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                .pattern(" D ")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', STRONG_MAGNET)
                .define('B', TITANIUM_ALLOY_INGOT)
                .define('C', ModItems.SUPER_CAPACITOR)
                .define('D', ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK)
                .unlockedBy("has_" + STRONG_MAGNET.getRegisteredName(), RegistrateRecipeProvider.has(STRONG_MAGNET))
                .unlockedBy("has_" + TITANIUM_ALLOY_INGOT.getRegisteredName(), RegistrateRecipeProvider.has(TITANIUM_ALLOY_INGOT))
                .unlockedBy("has_" + ModItems.SUPER_CAPACITOR.getRegisteredName(), RegistrateRecipeProvider.has(ModItems.SUPER_CAPACITOR))
                .unlockedBy("has_" + ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK.getRegisteredName(), RegistrateRecipeProvider.has(ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK))
                .save(prov);
        })
        .register();

    public static final ItemEntry<AnvilonEmissionTubeItem> ANVILON_EMISSION_TUBE = NekoPlus.REGISTRATE
        .item("anvilon_emission_tube", AnvilonEmissionTubeItem::new)
        .recipe((ctx, prov) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("ABA")
                .pattern("C D")
                .pattern("A A")
                .define('A', ModItems.ROYAL_STEEL_INGOT)
                .define('B', INTEGRATED_CHIP_CIRCUIT_BOARD)
                .define('C', Items.ANVIL)
                .define('D', CARBON_DIOXIDE_LASER_TUBE)
                .unlockedBy("has_" + prov.safeName(ModItems.ROYAL_STEEL_INGOT), RegistrateRecipeProvider.has(ModItems.ROYAL_STEEL_INGOT))
                .unlockedBy("has_" + prov.safeName(INTEGRATED_CHIP_CIRCUIT_BOARD), RegistrateRecipeProvider.has(INTEGRATED_CHIP_CIRCUIT_BOARD))
                .unlockedBy("has_" + prov.safeName(Items.ANVIL), RegistrateRecipeProvider.has(Items.ANVIL))
                .unlockedBy("has_" + prov.safeName(CARBON_DIOXIDE_LASER_TUBE), RegistrateRecipeProvider.has(CARBON_DIOXIDE_LASER_TUBE))
                .save(prov, NekoPlus.location("anvilon_emission_tube_plain"));

        })
        .model((ctx, prov) -> {
        })
        .register();

    public static final ItemEntry<Item> GUMMY_BEAR = NekoPlus.REGISTRATE
        .item("gummy_bear", Item::new)
        .properties(p -> p.food(
            new FoodProperties.Builder()
                .alwaysEdible()
                .fast()
                .nutrition(2)
                .saturationModifier(2)
                .effect(() -> new MobEffectInstance(MobEffects.SATURATION, 20), 1f)
                .build()
        ))
        .model((ctx, prov) -> {
        })
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

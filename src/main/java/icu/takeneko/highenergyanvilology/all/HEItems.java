package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.ChargerChargingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.SuperHeatingRecipe;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.item.MageneticConfinementVesselItem;
import icu.takeneko.highenergyanvilology.recipes.AirCondensingRecipe;
import icu.takeneko.highenergyanvilology.util.DataGenUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class HEItems {
    static {
        HEAnvilology.REGISTRATE.defaultCreativeTab(HECreativeTabs.TAB.getKey());
    }

    public static final ItemEntry<Item> STRONG_MAGNET = HEAnvilology.REGISTRATE
        .item("strong_magnet", Item::new)
        .recipe((c, p) ->
            ChargerChargingRecipe.builder()
                .requires(ModItems.EARTH_CORE_SHARD)
                .result(c.get())
                .power(-64)
                .time(20 * 2)
                .save(p, p.safeId(c.get()) + "_charging")
        )
        .register();


    public static final ItemEntry<Item> TITANIUM_ALLOY_INGOT = HEAnvilology.REGISTRATE
        .item("titanium_alloy_ingot", Item::new)
        .recipe((c, p) -> {
            SuperHeatingRecipe.builder()
                .requires(ModItemTags.TITANIUM_INGOTS)
                .requires(Items.IRON_INGOT)
                .result(c.get(), 2)
                .save(p, p.safeId(c.get()) + "_superheating");
            p.storage(c, RecipeCategory.MISC, HEBlocks.TITANIUM_ALLOY_BLOCK);
        })
        .register();

    public static final ItemEntry<Item> TITANIUM_ALLOY_NUGGET = HEAnvilology.REGISTRATE
        .item("titanium_alloy_nugget", Item::new)
        .recipe((c, p) ->
            p.storage(c, RecipeCategory.MISC, TITANIUM_ALLOY_INGOT)
        )
        .register();

    public static final ItemEntry<MageneticConfinementVesselItem> MAGNETIC_CONFINEMENT_VESSEL = HEAnvilology.REGISTRATE
        .item("magnetic_confinement_vessel", MageneticConfinementVesselItem::new)
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

    public static final ItemEntry<Item> DRY_ICE = HEAnvilology.REGISTRATE
        .item("dry_ice", Item::new)
        .tag(HETags.Items.ICES, HETags.Items.DRY_ICES)
        .register();

    public static final ItemEntry<Item> SULFUR = HEAnvilology.REGISTRATE
        .item("sulfur", Item::new)
        .tag(Tags.Items.DUSTS, HETags.Items.SULFUR)
        .recipe((ctx, prov) -> {
            AirCondensingRecipe.builder()
                .dimension(prov.resolve(BuiltinDimensionTypes.NETHER))
                .results(List.of(new ItemStack(ctx.get(), 8), new ItemStack(DRY_ICE.asItem(), 1)))
                .probability(ConstantValue.exactly(0.3f))
                .ticks(10)
                .build()
                .save(HEAnvilology.location("air_condensing/nether"), prov);
        })
        .register();

    public static final ItemEntry<Item> CRYOCOOLER = HEAnvilology.REGISTRATE
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

    public static void setupRegistration() {
    }
}

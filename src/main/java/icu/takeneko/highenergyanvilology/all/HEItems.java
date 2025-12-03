package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.init.item.ModItems;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.SuperHeatingRecipe;
import dev.dubhe.anvilcraft.util.DataGenUtil;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

public class HEItems {
    static {
        HEAnvilology.REGISTRATE.defaultCreativeTab(HECreativeTabs.TAB.getKey());
    }

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

    public static final ItemEntry<Item> CRYOCOOLER = HEAnvilology.REGISTRATE
            .item("cryocooler", Item::new)
            .model(DataGenUtil::noExtraModelOrState)
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

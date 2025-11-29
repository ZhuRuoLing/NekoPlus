package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.SuperHeatingRecipe;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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


    public static void setupRegistration() {
    }
}

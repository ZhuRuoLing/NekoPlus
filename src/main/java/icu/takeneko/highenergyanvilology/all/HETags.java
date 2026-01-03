package icu.takeneko.highenergyanvilology.all;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class HETags {
    public static class Blocks {
        //region common
        public static final TagKey<Block> STORAGE_BLOCKS_TITANIUM_ALLOY = storageBlocks("titanium_alloy");
        public static final TagKey<Block> STORAGE_BLOCKS_SILICON = storageBlocks("silicon");
        //endregion

        //region util
        public static @NotNull TagKey<Block> c(String id) {
            return TagKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath("c", id)
            );
        }

        public static @NotNull TagKey<Block> storageBlocks(String material) {
            return c("storage_blocks/" + material);
        }
        //endregion
    }

    public static class Items {
        //region common
        public static final TagKey<Item> SILICON = c("silicon");
        public static final TagKey<Item> STORAGE_BLOCKS_SILICON = storageBlocks("silicon");
        public static final TagKey<Item> STORAGE_BLOCKS_TITANIUM_ALLOY = storageBlocks("titanium_alloy");
        public static final TagKey<Item> SULFUR = c("dusts/sulfur");
        public static final TagKey<Item> DRY_ICES = c("dry_ices");
        public static final TagKey<Item> SILVER_PLATE = c("plates/silver");
        //endregion

        //region util
        public static @NotNull TagKey<Item> c(String id) {
            return TagKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath("c", id)
            );
        }

        public static @NotNull TagKey<Item> storageBlocks(String material) {
            return c("storage_blocks/" + material);
        }
        //endregion
    }
}

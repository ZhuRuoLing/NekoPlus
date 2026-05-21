package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class NPTags {
    public static class Blocks {
        //region common
        public static final TagKey<Block> STORAGE_BLOCKS_TITANIUM_ALLOY = storageBlocks("titanium_alloy");
        public static final TagKey<Block> STORAGE_BLOCKS_SILICON = storageBlocks("silicon");
        public static final TagKey<Block> LIGHTWEIGHT_BLOCK = he("lightweight_block");
        //endregion

        //region util
        public static @NotNull TagKey<Block> c(String id) {
            return TagKey.create(
                    Registries.BLOCK,
                    Identifier.fromNamespaceAndPath("c", id)
            );
        }

        public static @NotNull TagKey<Block> he(String id) {
            return TagKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(NekoPlus.MODID, id)
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
                    Identifier.fromNamespaceAndPath("c", id)
            );
        }

        public static @NotNull TagKey<Item> storageBlocks(String material) {
            return c("storage_blocks/" + material);
        }
        //endregion
    }
}

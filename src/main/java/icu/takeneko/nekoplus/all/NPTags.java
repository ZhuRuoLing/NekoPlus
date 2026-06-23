package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class NPTags {
    public static class Blocks {
        public static final TagKey<Block> STORAGE_BLOCKS_TITANIUM_ALLOY = storageBlocks("titanium_alloy");
        public static final TagKey<Block> STORAGE_BLOCKS_SILICON = storageBlocks("silicon");
        public static final TagKey<Block> LIGHTWEIGHT_BLOCK = np("lightweight_block");
        public static final TagKey<Block> NESTED_SHULKER_BLOCK = np("nested_shulker_box");
        
        public static @NotNull TagKey<Block> c(String id) {
            return TagKey.create(
                    Registries.BLOCK,
                    Identifier.fromNamespaceAndPath("c", id)
            );
        }

        public static @NotNull TagKey<Block> np(String id) {
            return TagKey.create(
                Registries.BLOCK,
                NekoPlus.location(id)
            );
        }

        public static @NotNull TagKey<Block> storageBlocks(String material) {
            return c("storage_blocks/" + material);
        }
        
    }

    public static class Items {
        public static final TagKey<Item> SILICON = c("silicon");
        public static final TagKey<Item> STORAGE_BLOCKS_SILICON = storageBlocks("silicon");
        public static final TagKey<Item> STORAGE_BLOCKS_TITANIUM_ALLOY = storageBlocks("titanium_alloy");
        public static final TagKey<Item> SULFUR = c("dusts/sulfur");
        public static final TagKey<Item> DRY_ICES = c("dry_ices");
        public static final TagKey<Item> SILVER_PLATE = c("plates/silver");
        public static final TagKey<Item> CHROME_BALLS = c("chromeballs");
        
        public static @NotNull TagKey<Item> c(String id) {
            return TagKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath("c", id)
            );
        }

        public static @NotNull TagKey<Item> storageBlocks(String material) {
            return c("storage_blocks/" + material);
        }
    }
    
    public static class Biomes {
        public static final TagKey<Biome> HAS_RUIN = np("has_structure/ruin");

        public static @NotNull TagKey<Biome> np(String id) {
            return TagKey.create(
                Registries.BIOME,
                NekoPlus.location(id)
            );
        }
    }
}

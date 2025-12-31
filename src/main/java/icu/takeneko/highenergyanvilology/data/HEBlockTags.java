package icu.takeneko.highenergyanvilology.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import icu.takeneko.highenergyanvilology.all.HEBlocks;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

@SuppressWarnings({"DataFlowIssue","unchecked"})
public class HEBlockTags {

    @SuppressWarnings("rawtypes")
    public static final TagKey[] IRON_PICKAXE_MINEABLE = new TagKey[]{
        BlockTags.INCORRECT_FOR_WOODEN_TOOL,
        BlockTags.INCORRECT_FOR_GOLD_TOOL,
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        BlockTags.NEEDS_IRON_TOOL,
        BlockTags.MINEABLE_WITH_PICKAXE,
        ModBlockTags.HAMMER_REMOVABLE
    };

    public static void setupBlockTags(RegistrateTagsProvider<Block> provider) {
        ironPickaxeMineableBlock(HEBlocks.ANVILON_EMITTER_BLOCK, provider);
        ironPickaxeMineableBlock(HEBlocks.PARTICLE_STABILIZER, provider);
        ironPickaxeMineableBlock(HEBlocks.TITANIUM_ALLOY_BLOCK, provider);
        ironPickaxeMineableBlock(HEBlocks.ROYAL_STEEL_CASING, provider);
        ironPickaxeMineableBlock(HEBlocks.STELLAR_ENGINE, provider);
        ironPickaxeMineableBlock(HEBlocks.TARDIS, provider);
        ironPickaxeMineableBlock(HEBlocks.HIGH_ENERGY_LASER, provider);
        ironPickaxeMineableBlock(HEBlocks.NETHERITE_SCRAP_BLOCK, provider);
        ironPickaxeMineableBlock(HEBlocks.TITANIUM_ALLOY_ANVIL, provider);

        wrenchableBlock(HEBlocks.ANVILON_EMITTER_BLOCK, provider);
        wrenchableBlock(HEBlocks.PARTICLE_STABILIZER, provider);
        wrenchableBlock(HEBlocks.ROYAL_STEEL_CASING, provider);
        wrenchableBlock(HEBlocks.STELLAR_ENGINE, provider);
        wrenchableBlock(HEBlocks.TARDIS, provider);
        wrenchableBlock(HEBlocks.HIGH_ENERGY_LASER, provider);
        wrenchableBlock(HEBlocks.TITANIUM_ALLOY_ANVIL, provider);

        provider.addTag(ModBlockTags.OVERSEER_BASE)
            .add(HEBlocks.ROYAL_STEEL_CASING.getKey());
    }

    public static void ironPickaxeMineableBlock(Holder<Block> holder, RegistrateTagsProvider<Block> provider) {
        for (TagKey<Block> tagKey : IRON_PICKAXE_MINEABLE) {
            provider.addTag(tagKey)
                .add(holder.getKey());
        }
    }

    public static void wrenchableBlock(Holder<Block> holder, RegistrateTagsProvider<Block> provider) {
        provider.addTag(ModBlockTags.HAMMER_REMOVABLE)
            .add(holder.getKey());
    }
}

package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumTagsProvider;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings({"DataFlowIssue", "unchecked"})
public class NPBlockTags {

    @SuppressWarnings("rawtypes")
    public static final TagKey[] IRON_PICKAXE_MINEABLE = new TagKey[]{
        BlockTags.INCORRECT_FOR_WOODEN_TOOL,
        BlockTags.INCORRECT_FOR_GOLD_TOOL,
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        BlockTags.NEEDS_IRON_TOOL,
        BlockTags.MINEABLE_WITH_PICKAXE,
        ModBlockTags.HAMMER_REMOVABLE
    };

    private static Identifier key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow().identifier();
    }

    public static void setupBlockTags(RegistrumTagsProvider<Block> provider) {
        ironPickaxeMineableBlock(NPBlocks.PARTICLE_STABILIZER, provider);
        ironPickaxeMineableBlock(NPBlocks.TITANIUM_ALLOY_BLOCK, provider);
        ironPickaxeMineableBlock(NPBlocks.ROYAL_STEEL_CASING, provider);
        ironPickaxeMineableBlock(NPBlocks.STELLAR_ENGINE, provider);
        ironPickaxeMineableBlock(NPBlocks.TARDIS, provider);
        ironPickaxeMineableBlock(NPBlocks.HIGH_ENERGY_LASER, provider);
        ironPickaxeMineableBlock(NPBlocks.NETHERITE_SCRAP_BLOCK, provider);
        ironPickaxeMineableBlock(NPBlocks.TITANIUM_ALLOY_ANVIL, provider);
        ironPickaxeMineableBlock(NPBlocks.SHULKER_HATCH_BLOCK, provider);

        wrenchableBlock(NPBlocks.PARTICLE_STABILIZER, provider);
        wrenchableBlock(NPBlocks.ROYAL_STEEL_CASING, provider);
        wrenchableBlock(NPBlocks.STELLAR_ENGINE, provider);
        wrenchableBlock(NPBlocks.TARDIS, provider);
        wrenchableBlock(NPBlocks.HIGH_ENERGY_LASER, provider);
        wrenchableBlock(NPBlocks.TITANIUM_ALLOY_ANVIL, provider);
        wrenchableBlock(NPBlocks.SHULKER_HATCH_BLOCK, provider);


        provider.rawBuilder(ModBlockTags.OVERSEER_BASE)
            .addElement(NPBlocks.ROYAL_STEEL_CASING.getKey().identifier());

        provider.rawBuilder(NPTags.Blocks.LIGHTWEIGHT_BLOCK)
            .addElement(key(Blocks.SLIME_BLOCK))
            .addElement(key(Blocks.HONEY_BLOCK))
            .addElement(key(Blocks.SCAFFOLDING))
            .addTag(BlockTags.LEAVES.location());

        provider.rawBuilder(NPTags.Blocks.NESTED_SHULKER_BLOCK)
            .addElement(ModBlocks.NESTING_SHULKER_BOX.getId())
            .addElement(ModBlocks.OVER_NESTING_SHULKER_BOX.getId())
            .addElement(ModBlocks.SUPERCRITICAL_NESTING_SHULKER_BOX.getId());
    }

    public static void ironPickaxeMineableBlock(Holder<Block> holder, RegistrumTagsProvider<Block> provider) {
        for (TagKey<Block> tagKey : IRON_PICKAXE_MINEABLE) {
            provider.rawBuilder(tagKey)
                .addElement(holder.getKey().identifier());
        }
    }

    public static void wrenchableBlock(Holder<Block> holder, RegistrumTagsProvider<Block> provider) {
        provider.rawBuilder(ModBlockTags.HAMMER_REMOVABLE)
            .addElement(holder.getKey().identifier());
    }
}

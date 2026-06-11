package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumTagsProvider;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
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
        ironPickaxeMineableBlock(
            provider,
            NPBlocks.PARTICLE_STABILIZER,
            NPBlocks.TITANIUM_ALLOY_BLOCK,
            NPBlocks.ROYAL_STEEL_CASING,
            NPBlocks.STELLAR_ENGINE,
            NPBlocks.TARDIS,
            NPBlocks.HIGH_ENERGY_LASER,
            NPBlocks.NETHERITE_SCRAP_BLOCK,
            NPBlocks.TITANIUM_ALLOY_ANVIL,
            NPBlocks.SHULKER_HATCH,
            NPBlocks.MINERAL_FOUNTAIN_PRESSURIZER,
            NPBlocks.CUT_TITANIUM_ALLOY_BLOCK,
            NPBlocks.CUT_TITANIUM_ALLOY_SLAB,
            NPBlocks.CUT_TITANIUM_ALLOY_STAIR,
            NPBlocks.BLAST_CRYSTAL
        );

        wrenchableBlock(
            provider,
            NPBlocks.PARTICLE_STABILIZER,
            NPBlocks.ROYAL_STEEL_CASING,
            NPBlocks.STELLAR_ENGINE,
            NPBlocks.TARDIS,
            NPBlocks.HIGH_ENERGY_LASER,
            NPBlocks.TITANIUM_ALLOY_ANVIL,
            NPBlocks.SHULKER_HATCH,
            NPBlocks.BLAST_CRYSTAL
        );

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

        provider.rawBuilder(ModBlockTags.LASER_CAN_PASS_THROUGH)
            .addElement(ModBlocks.SPECTRAL_ANVIL.getId());
    }

    public static void ironPickaxeMineableBlock(RegistrumTagsProvider<Block> provider, Holder<Block>... holder) {
        for (TagKey<Block> tagKey : IRON_PICKAXE_MINEABLE) {
            tag(provider, tagKey, holder);
        }
    }

    public static void wrenchableBlock(RegistrumTagsProvider<Block> provider, Holder<Block>... holder) {
        tag(provider, ModBlockTags.HAMMER_REMOVABLE, holder);
    }

    public static void tag(RegistrumTagsProvider<Block> provider, TagKey<Block> tagKey, Holder<Block>... holder) {
        TagBuilder tagBuilder = provider.rawBuilder(tagKey);
        for (Holder<Block> it : holder) {
            tagBuilder.addElement(it.getKey().identifier());
        }
    }
}

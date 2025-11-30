package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.util.entry.BlockEntry;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.block.AnvilonEmitterBlock;
import icu.takeneko.highenergyanvilology.util.ModelUtils;
import icu.takeneko.highenergyanvilology.util.StateUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;

public class HEBlocks {

    static {
        HEAnvilology.REGISTRATE.defaultCreativeTab(HECreativeTabs.TAB.getKey());
    }

    public static final BlockEntry<AnvilonEmitterBlock> ANVILON_EMITTER_BLOCK = HEAnvilology.REGISTRATE
        .block("anvilon_emitter", AnvilonEmitterBlock::new)
        .properties(prop -> Blocks.IRON_BLOCK.properties()
            .noOcclusion()
            .isRedstoneConductor(StateUtils::always)
            .isSuffocating(StateUtils::never)
            .isViewBlocking(StateUtils::never)
        )
        .defaultBlockstate()
        .blockstate((ctx, cons) -> {
            cons.simpleBlock(
                ctx.get(),
                cons.models()
                    .getBuilder("block/anvilon_emitter")
                    .texture("particle", "highenergyanvilology:block/anvilon_emitter")
            );
        })
        .defaultLang()
        .defaultLoot()
        .item()
        .model((ctx, prov) -> {
            ModelUtils.wrapDefaultBlockItemTransform(
                prov.getBuilder(ctx.getName())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
            );
        })
        .build()
        .register();

    public static final BlockEntry<Block> TITANIUM_ALLOY_BLOCK = HEAnvilology.REGISTRATE
        .block("titanium_alloy_block", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .tag(Tags.Blocks.STORAGE_BLOCKS, HETags.Blocks.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .item()
        .tag(Tags.Items.STORAGE_BLOCKS, HETags.Items.STORAGE_BLOCKS_TITANIUM_ALLOY)
        .build()
        .register();

    public static final BlockEntry<Block> ROYAL_STEEL_CASING = HEAnvilology.REGISTRATE
        .block("royal_steel_casing", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .item()
        .build()
        .register();

    public static void setupRegistration() {
    }
}

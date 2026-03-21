package icu.takeneko.nekoplus.util;

import dev.anvilcraft.lib.v2.registrum.providers.DataGenContext;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumBlockstateProvider;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumItemModelProvider;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class DataGenUtils {
    private DataGenUtils() {
    }

    public static <T extends Block> void existingBlockModel(
        DataGenContext<Block, T> ctx,
        RegistrumBlockstateProvider prov
    ) {
        prov.getVariantBuilder(ctx.get())
            .partialState()
            .addModels(
                ConfiguredModel.builder()
                    .modelFile(new ModelFile.ExistingModelFile(prov.modLoc("block/" + ctx.getName()), prov.models().existingFileHelper))
                    .build()
            );
    }

    public static void customRenderer(
        DataGenContext<?, ?> ctx,
        RegistrumItemModelProvider prov
    ) {
        prov.getBuilder(ctx.getName())
            .parent(new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("builtin/entity")))
            .transforms()
            .transform(ItemDisplayContext.GUI).rotation(15, -30, 0).translation(0, 0, 0).scale(0.625f, 0.625f, 0.625f).end()
            .transform(ItemDisplayContext.GROUND).rotation(0, 2, 0).translation(0, 3, 0).scale(0.5f, 0.5f, 0.5f).end()
            .transform(ItemDisplayContext.FIXED).rotation(0, 180, 0).translation(0, 0, 0).scale(0, 0, 0).end()
            .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, 225, 0).translation(0, 2.75f, 1).scale(0.55f, 0.55f, 0.55f).end()
            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.75f, 1).scale(0.55f, 0.55f, 0.55f).end()
            .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(1.13f, 3.2f, 1.13f).scale(0.55f, 0.55f, 0.55f).end()
            .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).translation(0, 0, 0).scale(0.55f, 0.55f, 0.55f).end();
    }

    public static <T extends RegistrumProvider> void emptyConsumer(
        DataGenContext<?, ?> ctx,
        T prov
    ) {
    }
}

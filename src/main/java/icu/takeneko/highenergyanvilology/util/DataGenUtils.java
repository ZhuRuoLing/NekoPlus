package icu.takeneko.highenergyanvilology.util;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class DataGenUtils {
    private DataGenUtils() {}

    public static void existingBlockModel(
        DataGenContext<Block, ?> ctx,
        RegistrateBlockstateProvider prov
    ) {
        prov.getVariantBuilder(ctx.get())
            .partialState()
            .addModels(
                ConfiguredModel.builder()
                    .modelFile(new ModelFile.ExistingModelFile(prov.modLoc( "block/"+ ctx.getName()), prov.models().existingFileHelper))
                    .build()
            );
    }

    public static <T extends RegistrateProvider> void emptyConsumer(
        DataGenContext<?, ?> ctx,
        T prov
    ) {}
}

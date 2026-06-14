package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.Registrum;
import dev.anvilcraft.lib.v2.registrum.providers.ProviderType;
import icu.takeneko.nekoplus.data.provider.NPLootTablesProvider;
import icu.takeneko.nekoplus.data.provider.NPTagsGen;
import icu.takeneko.nekoplus.data.provider.NPRecipesGen;
import icu.takeneko.nekoplus.data.provider.NPSounds;
import icu.takeneko.nekoplus.data.provider.NPTradeSets;
import icu.takeneko.nekoplus.data.provider.NPTranslations;
import icu.takeneko.nekoplus.data.provider.NPVillagerTrades;
import icu.takeneko.nekoplus.data.provider.NPWorldGen;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class NPDataGen {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.VILLAGER_TRADE, NPVillagerTrades::bootstrap)
        .add(Registries.TRADE_SET, NPTradeSets::bootstrap)
        .add(Registries.STRUCTURE, NPWorldGen::bootstrapStructures)
        .add(Registries.STRUCTURE_SET, NPWorldGen::bootstrapStructureSets);

    public static void setupDataGeneration(Registrum registrum) {
        registrum.addDataGenerator(ProviderType.BLOCK_TAGS, NPTagsGen::setupBlockTags);
        registrum.addDataGenerator(ProviderType.LANG, NPTranslations::addTranslations);
        registrum.addDataGenerator(ProviderType.RECIPE, NPRecipesGen::addRecipes);
    }

    @SubscribeEvent
    public static void on(GatherDataEvent.Client event) {
        event.createProvider(NPSounds::new);
        event.createDatapackRegistryObjects(BUILDER);
        event.createProvider(NPLootTablesProvider::new);
        event.createProvider(NPTagsGen.BiomesProvider::new);
    }
}

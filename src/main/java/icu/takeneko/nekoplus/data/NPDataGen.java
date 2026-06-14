package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.Registrum;
import dev.anvilcraft.lib.v2.registrum.providers.ProviderType;
import icu.takeneko.nekoplus.data.provider.NPRecipesData;
import icu.takeneko.nekoplus.data.provider.NPSounds;
import icu.takeneko.nekoplus.data.provider.NPTradeSets;
import icu.takeneko.nekoplus.data.provider.NPTranslations;
import icu.takeneko.nekoplus.data.provider.NPVillagerTrades;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class NPDataGen {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.VILLAGER_TRADE, NPVillagerTrades::bootstrap)
        .add(Registries.TRADE_SET, NPTradeSets::bootstrap);

    public static void setupDataGeneration(Registrum registrum) {
        registrum.addDataGenerator(ProviderType.BLOCK_TAGS, NPBlockTags::setupBlockTags);
        registrum.addDataGenerator(ProviderType.LANG, NPTranslations::addTranslations);
        registrum.addDataGenerator(ProviderType.RECIPE, NPRecipesData::addRecipes);
    }

    @SubscribeEvent
    public static void on(GatherDataEvent.Client event) {
        event.createProvider(NPSounds::new);
        event.createDatapackRegistryObjects(BUILDER);
    }
}

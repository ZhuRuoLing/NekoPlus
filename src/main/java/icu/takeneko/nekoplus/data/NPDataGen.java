package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.Registrum;
import dev.anvilcraft.lib.v2.registrum.providers.ProviderType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class NPDataGen {
    public static void setupDataGeneration(Registrum registrum) {
        registrum.addDataGenerator(ProviderType.BLOCK_TAGS, NPBlockTags::setupBlockTags);
        registrum.addDataGenerator(ProviderType.LANG, NPTranslations::addTranslations);
        registrum.addDataGenerator(ProviderType.RECIPE, NPRecipesData::addRecipes);
    }

    @SubscribeEvent
    public static void on(GatherDataEvent.Client event) {
        event.createProvider(NPSounds::new);
    }
}

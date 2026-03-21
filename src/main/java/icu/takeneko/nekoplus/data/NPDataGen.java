package icu.takeneko.nekoplus.data;

import dev.anvilcraft.lib.v2.registrum.Registrum;
import dev.anvilcraft.lib.v2.registrum.providers.ProviderType;

public class NPDataGen {
    public static void setupDataGeneration(Registrum Registrum) {
        Registrum.addDataGenerator(ProviderType.BLOCK_TAGS, NPBlockTags::setupBlockTags);
        Registrum.addDataGenerator(ProviderType.LANG, NPTranslations::addTranslations);
        Registrum.addDataGenerator(ProviderType.RECIPE, NPRecipes::addRecipes);
    }
}

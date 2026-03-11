package icu.takeneko.nekoplus.data;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;

public class NPDataGen {
    public static void setupDataGeneration(Registrate registrate) {
        registrate.addDataGenerator(ProviderType.BLOCK_TAGS, NPBlockTags::setupBlockTags);
        registrate.addDataGenerator(ProviderType.LANG, NPTranslations::addTranslations);
        registrate.addDataGenerator(ProviderType.RECIPE, NPRecipes::addRecipes);
    }
}

package icu.takeneko.highenergyanvilology.data;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;

public class HEDataGen {
    public static void setupDataGeneration(Registrate registrate) {
        registrate.addDataGenerator(ProviderType.BLOCK_TAGS, HEBlockTags::setupBlockTags);
        registrate.addDataGenerator(ProviderType.LANG, HETranslations::addTranslations);
        registrate.addDataGenerator(ProviderType.RECIPE, HERecipes::addRecipes);
    }
}

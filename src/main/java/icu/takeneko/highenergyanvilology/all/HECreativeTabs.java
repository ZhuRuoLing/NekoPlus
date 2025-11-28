package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HECreativeTabs {
    public static final DeferredRegister<CreativeModeTab> DR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HEAnvilology.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = DR.register(
        "tab",
        () -> CreativeModeTab.builder()
            .title(HEAnvilology.REGISTRATE.addRawLang("itemGroup.highenergyanvilology.tab", "AnvilCraft: High Energy Anvilology"))
            .icon(HEBlocks.ANVILON_EMITTER_BLOCK.asItem()::getDefaultInstance)
            .build()
    );
}

package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NPCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> DR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NekoPlus.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = DR.register(
        "tab",
        () -> CreativeModeTab.builder()
            .title(NekoPlus.REGISTRUM.addRawLang("itemGroup.nekoplus.tab", "AnvilCraft: High Energy Anvilology"))
            .icon(NPBlocks.TARDIS.asItem()::getDefaultInstance)
            .build()
    );

}


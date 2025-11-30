package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.foundation.ui.BlockUI;
import icu.takeneko.highenergyanvilology.ui.menu.AnvilonEmitterMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HEMenuTypes {
    public static final DeferredRegister<MenuType<?>> DR = DeferredRegister.create(BuiltInRegistries.MENU, HEAnvilology.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AnvilonEmitterMenu>> ANVILON_EMITTER = DR.register(
        "anvilon_emitter",
        () -> IMenuTypeExtension.create((windowId, inv, data) -> BlockUI.create(
            HEMenuTypes.ANVILON_EMITTER.get(),
            windowId,
            inv,
            data,
             AnvilonEmitterMenu::new
        ))
    );
}

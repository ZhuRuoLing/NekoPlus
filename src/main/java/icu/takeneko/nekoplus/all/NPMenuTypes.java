package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NPMenuTypes {
    public static final DeferredRegister<MenuType<?>> DR = DeferredRegister.create(BuiltInRegistries.MENU, NekoPlus.MODID);

}

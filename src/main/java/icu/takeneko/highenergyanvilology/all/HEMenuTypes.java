package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HEMenuTypes {
    public static final DeferredRegister<MenuType<?>> DR = DeferredRegister.create(BuiltInRegistries.MENU, HEAnvilology.MODID);

}

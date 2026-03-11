package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.material.AnvilMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class NPRegistries {
    public static final ResourceKey<Registry<AnvilMaterial>> MATERIAL = ResourceKey.createRegistryKey(NekoPlus.location("material"));
}

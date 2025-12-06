package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class HERegistries {
    public static final ResourceKey<Registry<AnvilMaterial>> MATERIAL = ResourceKey.createRegistryKey(HEAnvilology.location("material"));
}

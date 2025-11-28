package icu.takeneko.highenergyanvilology.all;

import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.material.AnvilMaterial;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HEAnvilMaterials {
    public static final DeferredRegister<AnvilMaterial> DR = DeferredRegister.create(HERegistries.MATERIAL, HEAnvilology.MODID);

    public static final AnvilMaterial IRON = register(
        "iron",
        AnvilMaterial.builder()
            .maxDamage(128)
            .processRate(1f)
            .materialAnvilItemRef(() -> Items.ANVIL)
            .build()
    );

    public static final AnvilMaterial SPECTRAL = register(
        "spectral",
        AnvilMaterial.builder()
            .maxDamage(-1)
            .processRate(0.8f)
            .materialAnvilItemRef(ModBlocks.SPECTRAL_ANVIL::asItem)
            .build()
    );

    public static final AnvilMaterial ROYAL = register(
        "royal",
        AnvilMaterial.builder()
            .maxDamage(-1)
            .processRate(2f)
            .materialAnvilItemRef(ModBlocks.ROYAL_ANVIL::asItem)
            .build()
    );

    public static final AnvilMaterial EMBER = register(
        "ember",
        AnvilMaterial.builder()
            .maxDamage(-1)
            .processRate(5f)
            .materialAnvilItemRef(ModBlocks.EMBER_ANVIL::asItem)
            .build()
    );

    private static AnvilMaterial register(
        String name,
        AnvilMaterial material
    ) {
        DR.register(name, () -> material);
        return material;
    }
}

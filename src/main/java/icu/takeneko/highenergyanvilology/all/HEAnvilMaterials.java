package icu.takeneko.highenergyanvilology.all;

import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilMaterial;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HEAnvilMaterials {
    public static final DeferredRegister<AnvilMaterial> DR = DeferredRegister.create(HERegistries.MATERIAL, HEAnvilology.MODID);

    public static final AnvilMaterial EMPTY = register(
        "empty",
        AnvilMaterial.builder()
            .maxDamage(0)
            .processRate(0)
            .color(-1)
            .materialAnvilItemRef(() -> Items.AIR)
            .build()
    );

    public static final AnvilMaterial IRON = register(
        "iron",
        AnvilMaterial.builder()
            .maxDamage(128)
            .processRate(1)
            .color(0x41403F)
            .materialAnvilItemRef(() -> Items.ANVIL)
            .build()
    );

    public static final AnvilMaterial SPECTRAL = register(
        "spectral",
        AnvilMaterial.builder()
            .maxDamage(-1)
            .processRate(0.8f)
            .color(0x576095)
            .materialAnvilItemRef(ModBlocks.SPECTRAL_ANVIL::asItem)
            .build()
    );

    public static final AnvilMaterial ROYAL = register(
        "royal",
        AnvilMaterial.builder()
            .maxDamage(-1)
            .processRate(2)
            .color(0x869676)
            .materialAnvilItemRef(ModBlocks.ROYAL_ANVIL::asItem)
            .build()
    );

    public static final AnvilMaterial EMBER = register(
        "ember",
        AnvilMaterial.builder()
            .maxDamage(-1)
            .processRate(3)
            .color(0xECC001)
            .materialAnvilItemRef(ModBlocks.EMBER_ANVIL::asItem)
            .build()
    );

    public static final AnvilMaterial TRANSCENDENCE = register(
        "transcendence",
        AnvilMaterial.builder()
            .maxDamage(-1)
            .processRate(5)
            .color(0xE384F9)
            .materialAnvilItemRef(ModBlocks.TRANSCENDENCE_ANVIL::asItem)
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

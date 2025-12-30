package icu.takeneko.highenergyanvilology.item;

import icu.takeneko.highenergyanvilology.all.HEAnvilMaterials;
import icu.takeneko.highenergyanvilology.all.HEBuiltinRegistries;
import icu.takeneko.highenergyanvilology.all.HEDataComponents;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilMaterial;
import icu.takeneko.highenergyanvilology.foundation.material.AnvilonType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MageneticConfinementVesselItem extends Item {
    public MageneticConfinementVesselItem(Properties properties) {
        super(
            properties
//                .component(
//                    HEDataComponents.CONTAINED_ANVILON_TYPE,
//                    HEAnvilMaterials.EMPTY
//                )
        );
    }

    @Override
    public Component getName(ItemStack stack) {
        AnvilMaterial material = stack.getOrDefault(HEDataComponents.CONTAINED_ANVILON_TYPE, HEAnvilMaterials.EMPTY);
        boolean hasContent = material != HEAnvilMaterials.EMPTY;
        if (!hasContent) {
            return super.getName(stack);
        }
        ResourceLocation key = HEBuiltinRegistries.MATERIAL.getKey(material);
        AnvilonType.Contained status = stack.getOrDefault(HEDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE);
        Component content = Component.translatable(
            "anvilon.highenergyanvilology.status." + status.getSerializedName(),
            Component.translatable(
                "anvilon." + key.getNamespace() + ".type." + key.getPath()
            )
        );

        return Component.translatable("item.highenergyanvilology.magnetic_confinement_vessel.full", content);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.get(HEDataComponents.CONTAINED_ANVILON_TYPE) != null && stack.get(HEDataComponents.CONTAINED_ANVILION_STATUS) == AnvilonType.Contained.UNSTABLE
            ? 1
            : 64;
    }
}

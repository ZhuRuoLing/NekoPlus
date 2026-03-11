package icu.takeneko.nekoplus.item;

import icu.takeneko.nekoplus.all.NPAnvilMaterials;
import icu.takeneko.nekoplus.all.NPBuiltinRegistries;
import icu.takeneko.nekoplus.all.NPDataComponents;
import icu.takeneko.nekoplus.foundation.material.AnvilMaterial;
import icu.takeneko.nekoplus.foundation.material.AnvilonType;
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
        AnvilMaterial material = stack.getOrDefault(NPDataComponents.CONTAINED_ANVILON_TYPE, NPAnvilMaterials.EMPTY);
        boolean hasContent = material != NPAnvilMaterials.EMPTY;
        if (!hasContent) {
            return super.getName(stack);
        }
        ResourceLocation key = NPBuiltinRegistries.MATERIAL.getKey(material);
        AnvilonType.Contained status = stack.getOrDefault(NPDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE);
        Component content = Component.translatable(
            "anvilon.nekoplus.status." + status.getSerializedName(),
            Component.translatable(
                "anvilon." + key.getNamespace() + ".type." + key.getPath()
            )
        );

        return Component.translatable("item.nekoplus.magnetic_confinement_vessel.full", content);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.get(NPDataComponents.CONTAINED_ANVILON_TYPE) != null && stack.get(NPDataComponents.CONTAINED_ANVILION_STATUS) == AnvilonType.Contained.UNSTABLE
            ? 1
            : 64;
    }
}

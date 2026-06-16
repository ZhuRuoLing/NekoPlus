package icu.takeneko.nekoplus.foundation.item.module.type;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import java.util.List;

public record GenericEnhancementModuleType<T extends NPEnhancementModule>(
    Identifier name,
    MapCodec<T> codec,
    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec,
    NPEnhancementModule.Factory<T> factory,
    Holder<Item> itemHolder,
    List<EquipmentSlotGroup> applicableItemSlots,
    int installationLimit
) implements NPEnhancementModuleType<T> {
    @Override
    public boolean appliesTo(ItemStack itemStack) {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable != null) {
            return applicableItemSlots.stream().allMatch(it -> it.test(equippable.slot()));
        }
        return applicableItemSlots.contains(EquipmentSlotGroup.HAND)
            || applicableItemSlots.contains(EquipmentSlotGroup.MAINHAND)
            || applicableItemSlots.contains(EquipmentSlotGroup.OFFHAND);
    }

    @Override
    public void postAssemble(ItemStack itemStack) {
    }
}

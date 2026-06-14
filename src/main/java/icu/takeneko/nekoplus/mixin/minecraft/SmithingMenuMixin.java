package icu.takeneko.nekoplus.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import icu.takeneko.nekoplus.all.NPDataComponents;
import icu.takeneko.nekoplus.all.NPItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    public SmithingMenuMixin(
        @Nullable MenuType<?> menuType,
        int containerId,
        Inventory inventory,
        ContainerLevelAccess access,
        ItemCombinerMenuSlotDefinition itemInputSlots
    ) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    @Shadow
    protected abstract SmithingRecipeInput createRecipeInput();

    @WrapOperation(
        method = "createInputSlotDefinitions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;withSlot(IIILjava/util/function/Predicate;)Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;",
            ordinal = 0
        )
    )
    private static ItemCombinerMenuSlotDefinition.Builder withEnhancementTemplate1(
        ItemCombinerMenuSlotDefinition.Builder instance,
        int slotIndex,
        int xPlacement,
        int yPlacement,
        Predicate<ItemStack> mayPlace,
        Operation<ItemCombinerMenuSlotDefinition.Builder> original
    ) {
        return original.call(
            instance,
            slotIndex,
            xPlacement,
            yPlacement,
            mayPlace.or(it -> it.is(NPItems.MODULAR_ENHANCEMENT_TEMPLATE))
        );
    }

    @WrapOperation(
        method = "createInputSlotDefinitions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;withSlot(IIILjava/util/function/Predicate;)Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;",
            ordinal = 1
        )
    )
    private static ItemCombinerMenuSlotDefinition.Builder withEnhancementTemplate2(
        ItemCombinerMenuSlotDefinition.Builder instance,
        int slotIndex,
        int xPlacement,
        int yPlacement,
        Predicate<ItemStack> mayPlace,
        Operation<ItemCombinerMenuSlotDefinition.Builder> original
    ) {
        return original.call(
            instance,
            slotIndex,
            xPlacement,
            yPlacement,
            mayPlace.or(it -> it.is(Tags.Items.TOOLS) || it.is(Tags.Items.ARMORS) || it.is(Items.ELYTRA))
        );
    }

    @WrapOperation(
        method = "createInputSlotDefinitions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;withSlot(IIILjava/util/function/Predicate;)Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;",
            ordinal = 2
        )
    )
    private static ItemCombinerMenuSlotDefinition.Builder withEnhancementTemplate3(
        ItemCombinerMenuSlotDefinition.Builder instance,
        int slotIndex,
        int xPlacement,
        int yPlacement,
        Predicate<ItemStack> mayPlace,
        Operation<ItemCombinerMenuSlotDefinition.Builder> original
    ) {
        return original.call(
            instance,
            slotIndex,
            xPlacement,
            yPlacement,
            mayPlace.or(it -> it.is(NPItems.ADVANCED_PROCESSOR))
        );
    }

    @Inject(
        method = "createResult",
        at = @At("HEAD"),
        cancellable = true
    )
    void handleModularResult(CallbackInfo ci) {
        SmithingRecipeInput input = this.createRecipeInput();
        ItemStack template = input.template();
        if (template.is(NPItems.MODULAR_ENHANCEMENT_TEMPLATE) && input.addition().is(NPItems.ADVANCED_PROCESSOR)) {
            this.resultSlots.setRecipeUsed(null);
            if (!input.base().has(NPDataComponents.ENHANCEMENT_MODULE)) {
                ItemStack copy = input.base().copyWithCount(1);
                copy.set(NPDataComponents.ENHANCEMENT_MODULE, List.of());
                this.resultSlots.setItem(0, copy);
                ci.cancel();
                return;
            }
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            ci.cancel();
        }
    }
}

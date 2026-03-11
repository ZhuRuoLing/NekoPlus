package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.material.AnvilMaterial;
import icu.takeneko.nekoplus.foundation.material.AnvilonType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class NPCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> DR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NekoPlus.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = DR.register(
        "tab",
        () -> CreativeModeTab.builder()
            .title(NekoPlus.REGISTRATE.addRawLang("itemGroup.nekoplus.tab", "AnvilCraft: High Energy Anvilology"))
            .icon(NPBlocks.ANVILON_EMITTER_BLOCK.asItem()::getDefaultInstance)
            .build()
    );

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void on(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().location().equals(TAB.getId())) return;
        for (Map.Entry<ResourceKey<AnvilMaterial>, AnvilMaterial> entry : NPBuiltinRegistries.MATERIAL.entrySet()) {
            AnvilMaterial material = entry.getValue();
            if (material == NPAnvilMaterials.EMPTY) continue;
            ItemStack stack = NPItems.MAGNETIC_CONFINEMENT_VESSEL.asStack();
            stack.set(NPDataComponents.CONTAINED_ANVILON_TYPE.get(), material);
            stack.set(NPDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.UNSTABLE);
            event.accept(stack);

            stack = stack.copy();
            stack.set(NPDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.STABLE);
            event.accept(stack);

            stack = stack.copy();
            stack.set(NPDataComponents.CONTAINED_ANVILION_STATUS, AnvilonType.Contained.ENTANGLED);
            stack.set(NPDataComponents.ENTANGLE_ANVILON_UUID, UUID.randomUUID());
            event.accept(stack);
        }
    }

}


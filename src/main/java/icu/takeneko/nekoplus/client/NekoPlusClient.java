package icu.takeneko.nekoplus.client;

import com.lowdragmc.lowdraglib2.editor.resource.EditorResourceEvent;
import com.lowdragmc.lowdraglib2.editor.resource.ResourceInstance;
import com.lowdragmc.lowdraglib2.editor.resource.TexturesResource;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import dev.dubhe.anvilcraft.api.rendering.CacheableBlockEntityRenderers;
import dev.dubhe.anvilcraft.init.block.ModBlockEntities;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.all.NPHammerTooltipProviders;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.client.extension.NPClientExtension;
import icu.takeneko.nekoplus.client.extension.NPClientRendererExtension;
import icu.takeneko.nekoplus.client.renderer.laser.NPLaserRenderer;
import icu.takeneko.nekoplus.foundation.block.tile.SpecialRendererBlock;
import icu.takeneko.nekoplus.ui.NPGuiResources;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.ArrayList;
import java.util.List;


@Mod(value = NekoPlus.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class NekoPlusClient {

    public NekoPlusClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        //container.registerExtensionPoint(IClientItemExtensions.class, new HEClientExtension());
        NPHammerTooltipProviders.setupRegistration();
    }

    @SubscribeEvent
    public static void on(FMLClientSetupEvent event) {
        NPLaserRenderer NPLaserRenderer = new NPLaserRenderer();
        CacheableBlockEntityRenderers.register(ModBlockEntities.RUBY_LASER.get(), NPLaserRenderer);
        CacheableBlockEntityRenderers.register(ModBlockEntities.RUBY_PRISM.get(), NPLaserRenderer);
        CacheableBlockEntityRenderers.register(NPBlockEntities.HIGH_ENERGY_LASER.get(), NPLaserRenderer);
    }

    @SubscribeEvent
    public static void on(RegisterClientExtensionsEvent event) {
        List<Item> bewlrItem = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof SpecialRendererBlock) {
                bewlrItem.add(item);
            }
        });
        event.registerItem(new NPClientExtension(bewlrItem), bewlrItem.reversed().toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void on(EditorResourceEvent.LoadBuiltin event){
        if (event.resourceInstance.resource == TexturesResource.INSTANCE){
            NPGuiResources.setupRegistration((ResourceInstance<IGuiTexture>)event.resourceInstance);
        }

    }

}

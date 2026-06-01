package icu.takeneko.nekoplus.client;

import com.lowdragmc.lowdraglib2.editor.resource.EditorResourceEvent;
import com.lowdragmc.lowdraglib2.editor.resource.ResourceInstance;
import com.lowdragmc.lowdraglib2.editor.resource.TexturesResource;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.UIElementRendererRegistry;
import dev.anvilcraft.lib.v2.rendering.cachedber.renderer.CachedBlockEntityRenderDispatcher;
import dev.dubhe.anvilcraft.client.renderer.laser.CachedLaserBlockEntityRenderer;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.all.NPHammerTooltipProviders;
import icu.takeneko.nekoplus.client.renderer.animation.NPMolangValues;
import icu.takeneko.nekoplus.foundation.client.ui.renderer.FourDirectionBlockDisplayElementRenderer;
import icu.takeneko.nekoplus.foundation.ui.widgets.FourDirectionBlockDisplayElement;
import icu.takeneko.nekoplus.ui.NPGuiResources;
import lombok.Getter;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = NekoPlus.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class NekoPlusClient {
    @Getter
    private static RecipeMap syncedRecipes;

    public NekoPlusClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        //container.registerExtensionPoint(IClientItemExtensions.class, new HEClientExtension());
        NPHammerTooltipProviders.setupRegistration();
    }

    @SubscribeEvent
    public static void on(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            UIElementRendererRegistry.register(
                FourDirectionBlockDisplayElement.class,
                new FourDirectionBlockDisplayElementRenderer()
            );
            CachedBlockEntityRenderDispatcher.INSTANCE.registerRenderer(
                NPBlockEntities.HIGH_ENERGY_LASER,
                new CachedLaserBlockEntityRenderer<>()
            );
            NPMolangValues.register();
        });
    }

    @SubscribeEvent
    public static void on(RecipesReceivedEvent event) {
        syncedRecipes = event.getRecipeMap();
    }

    @SubscribeEvent
    public static void on(EditorResourceEvent.LoadBuiltin event) {
        if (event.resourceInstance.resource == TexturesResource.INSTANCE) {
            NPGuiResources.setupRegistration((ResourceInstance<IGuiTexture>) event.resourceInstance);
        }
    }

}

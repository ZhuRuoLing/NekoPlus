package icu.takeneko.highenergyanvilology.client;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod(value = HEAnvilology.MODID, dist = Dist.CLIENT)
public class HEAnvilologyClient {
    public HEAnvilologyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}

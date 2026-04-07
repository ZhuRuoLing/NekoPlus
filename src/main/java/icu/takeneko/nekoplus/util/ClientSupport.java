package icu.takeneko.nekoplus.util;

import dev.dubhe.anvilcraft.util.FullBrightLevelProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ClientSupport {
    private static Level currentLevel;
    private static BlockAndTintGetter fullBrightLevel;

    @Nullable
    public static BlockAndTintGetter getFullBrightLevel() {
        if (Minecraft.getInstance().level == null) {
            return null;
        }
        if (currentLevel != Minecraft.getInstance().level) {
            currentLevel = Minecraft.getInstance().level;
            fullBrightLevel = new FullBrightLevelProxy(Minecraft.getInstance().level);
        }
        return fullBrightLevel;
    }
}

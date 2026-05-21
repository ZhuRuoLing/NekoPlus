package icu.takeneko.nekoplus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ClientSupport {
    private static Level currentLevel;
    private static BlockAndTintGetter fullBrightLevel = BlockAndTintGetter.EMPTY;

    @Nullable
    public static BlockAndTintGetter getFullBrightLevel() {
        if (Minecraft.getInstance().level == null) {
            return null;
        }
//        if (currentLevel != Minecraft.getInstance().level) {
//            currentLevel = Minecraft.getInstance().level;
//            fullBrightLevel = Block
//        }
        return fullBrightLevel;
    }
}

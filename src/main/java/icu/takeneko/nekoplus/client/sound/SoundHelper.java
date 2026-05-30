package icu.takeneko.nekoplus.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class SoundHelper {
    public static void playRejectSound(Vec3 pos, float volume) {
        Minecraft.getInstance().level.playLocalSound(
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.NOTE_BLOCK_BASS.value(),
            SoundSource.BLOCKS,
            volume,
            0.5612310241546865f,
            false
        );
    }
}

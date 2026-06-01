package icu.takeneko.nekoplus.mixin.minecraft;

import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.LevelEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelEventHandler.class)
public class LevelEventHandlerMixin {
    @Shadow
    @Final
    private ClientLevel level;

    @Inject(method = "levelEvent", at = @At("HEAD"), cancellable = true)
    public void levelEvent(int eventType, BlockPos pos, int data, CallbackInfo ci) {
        if (level.getBlockState(pos).is(NPBlocks.CAT_ANVIL)) {
            switch (eventType) {
                case LevelEvent.SOUND_ANVIL_BROKEN -> {
                    level.playLocalSound(
                        pos,
                        NPSoundEvents.CAT_ANVIL_BREAK.get(),
                        SoundSource.BLOCKS,
                        1f,
                        1f,
                        false
                    );
                    ci.cancel();
                }
                case LevelEvent.SOUND_ANVIL_LAND -> {
                    level.playLocalSound(
                        pos,
                        NPSoundEvents.CAT_ANVIL_LAND.get(),
                        SoundSource.BLOCKS,
                        1f,
                        1f,
                        false
                    );
                    ci.cancel();
                }
                case LevelEvent.SOUND_ANVIL_USED -> {
                    level.playLocalSound(
                        pos,
                        NPSoundEvents.CAT_ANVIL_USE.get(),
                        SoundSource.BLOCKS,
                        1f,
                        1f,
                        false
                    );
                    ci.cancel();
                }
            }
        }
    }
}

package icu.takeneko.nekoplus.client.sound;

import icu.takeneko.nekoplus.foundation.client.sound.LoopingSoundController;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LoopingBlockSoundInstance<T extends BlockEntity> extends AbstractTickableSoundInstance {
    private final T blockEntity;
    private final LoopingSoundController controller;

    public LoopingBlockSoundInstance(
        SoundEvent p_235076_,
        SoundSource p_235077_,
        T blockEntity,
        LoopingSoundController controller
    ) {
        super(p_235076_, p_235077_, SoundInstance.createUnseededRandom());
        this.controller = controller;
        this.looping = true;
        this.blockEntity = blockEntity;
        this.attenuation = Attenuation.LINEAR;
        this.delay = 0;
        this.x = blockEntity.getBlockPos().getX();
        this.y = blockEntity.getBlockPos().getY();
        this.z = blockEntity.getBlockPos().getZ();
    }

    public void stopNow() {
        this.stop();
    }

    @Override
    public void tick() {
        if (blockEntity.isRemoved() || controller.shouldSoundStop()) {
            this.stop();
            return;
        }
        this.x = blockEntity.getBlockPos().getX();
        this.y = blockEntity.getBlockPos().getY();
        this.z = blockEntity.getBlockPos().getZ();
        this.volume = 1.0F;
        this.pitch = 1.0F;
    }
}

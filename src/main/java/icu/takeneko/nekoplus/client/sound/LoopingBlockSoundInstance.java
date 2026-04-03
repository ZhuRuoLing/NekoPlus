package icu.takeneko.nekoplus.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LoopingBlockSoundInstance extends AbstractTickableSoundInstance {
    private final BlockEntity blockEntity;

    public LoopingBlockSoundInstance(SoundEvent p_235076_, SoundSource p_235077_, BlockEntity blockEntity) {
        super(p_235076_, p_235077_, SoundInstance.createUnseededRandom());
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
        if (blockEntity.isRemoved()) {
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

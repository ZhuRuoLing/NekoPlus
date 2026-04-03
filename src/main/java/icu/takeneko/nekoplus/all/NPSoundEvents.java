package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NPSoundEvents {
    public static final DeferredRegister<SoundEvent> DR = DeferredRegister.create(Registries.SOUND_EVENT, NekoPlus.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PARTICLE_STABILIZER_WORKING = DR.register(
        "block.particle_stabilizer.working",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.particle_stabilizer.working"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> INTERRUPT = DR.register(
        "block.interrupt",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.interrupt"))
    );
}

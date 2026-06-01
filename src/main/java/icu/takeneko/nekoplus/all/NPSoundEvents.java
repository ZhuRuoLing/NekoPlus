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

    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_BASE = DR.register(
        "block.cat_anvil.base",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.base"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_BREAK = DR.register(
        "block.cat_anvil.break",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.break"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_STEP = DR.register(
        "block.cat_anvil.step",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.step"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_PLACE = DR.register(
        "block.cat_anvil.place",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.place"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_HIT = DR.register(
        "block.cat_anvil.hit",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.hit"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_FALL = DR.register(
        "block.cat_anvil.fall",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.fall"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_LAND = DR.register(
        "block.cat_anvil.land",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.land"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> CAT_ANVIL_USE = DR.register(
        "block.cat_anvil.use",
        () -> SoundEvent.createVariableRangeEvent(NekoPlus.location("block.cat_anvil.use"))
    );
}

package icu.takeneko.nekoplus.data;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class NPSounds extends SoundDefinitionsProvider {
    public NPSounds(PackOutput output) {
        super(output, NekoPlus.MODID);
    }

    private static final int CAT_ANVIL_SOUND_MEOW_WEIGHT = 99*3;
    private static final int CAT_ANVIL_SOUND_HISS_WEIGHT = 1*4;
    private static final double CAT_ANVIL_SOUND_MEOW_VOLUME = 0.7;
    private static final double CAT_ANVIL_SOUND_HISS_VOLUME = 0.25;

    @Override
    public void registerSounds() {
        this.add(
            NPSoundEvents.PARTICLE_STABILIZER_WORKING.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.particle_stabilizer.working")
                .with(
                    sound(NekoPlus.location("block/particle_stabilizer/working"))
                        .attenuationDistance(10)
                )
        );
        this.add(
            NPSoundEvents.INTERRUPT.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.interrupt")
                .with(
                    sound(NekoPlus.location("block/interrupt"))
                        .attenuationDistance(10)
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_BASE.unwrapKey().orElseThrow().identifier(),
            definition()
                .with(
                    sound("mob/cat/meow1").volume(CAT_ANVIL_SOUND_MEOW_VOLUME).weight(CAT_ANVIL_SOUND_MEOW_WEIGHT),
                    sound("mob/cat/meow2").volume(CAT_ANVIL_SOUND_MEOW_VOLUME).weight(CAT_ANVIL_SOUND_MEOW_WEIGHT),
                    sound("mob/cat/meow3").volume(CAT_ANVIL_SOUND_MEOW_VOLUME).weight(CAT_ANVIL_SOUND_MEOW_WEIGHT),
                    sound("mob/cat/meow4").volume(CAT_ANVIL_SOUND_MEOW_VOLUME).weight(CAT_ANVIL_SOUND_MEOW_WEIGHT),
                    sound("mob/cat/hiss1").volume(CAT_ANVIL_SOUND_HISS_VOLUME).weight(CAT_ANVIL_SOUND_HISS_WEIGHT),
                    sound("mob/cat/hiss2").volume(CAT_ANVIL_SOUND_HISS_VOLUME).weight(CAT_ANVIL_SOUND_HISS_WEIGHT),
                    sound("mob/cat/hiss3").volume(CAT_ANVIL_SOUND_HISS_VOLUME).weight(CAT_ANVIL_SOUND_HISS_WEIGHT)
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_BREAK.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.generic.break")
                .with(
                    sound(
                        NPSoundEvents.CAT_ANVIL_BASE.getId(),
                        SoundDefinition.SoundType.EVENT
                    )
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_STEP.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                    sound(
                        NPSoundEvents.CAT_ANVIL_BASE.getId(),
                        SoundDefinition.SoundType.EVENT
                    )
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_PLACE.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.generic.place")
                .with(
                    sound(
                        NPSoundEvents.CAT_ANVIL_BASE.getId(),
                        SoundDefinition.SoundType.EVENT
                    )
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_HIT.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.generic.hit")
                .with(
                    sound(
                        NPSoundEvents.CAT_ANVIL_BASE.getId(),
                        SoundDefinition.SoundType.EVENT
                    )
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_FALL.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.generic.fall")
                .with(
                    sound(
                        NPSoundEvents.CAT_ANVIL_BASE.getId(),
                        SoundDefinition.SoundType.EVENT
                    )
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_LAND.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.anvil.land")
                .with(
                    sound(
                        NPSoundEvents.CAT_ANVIL_BASE.getId(),
                        SoundDefinition.SoundType.EVENT
                    )
                )
        );
        this.add(
            NPSoundEvents.CAT_ANVIL_USE.unwrapKey().orElseThrow().identifier(),
            definition()
                .subtitle("subtitles.block.anvil.use")
                .with(
                    sound(
                        NPSoundEvents.CAT_ANVIL_BASE.getId(),
                        SoundDefinition.SoundType.EVENT
                    )
                )
        );
    }
}

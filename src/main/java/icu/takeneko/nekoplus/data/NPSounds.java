package icu.takeneko.nekoplus.data;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class NPSounds extends SoundDefinitionsProvider {
    public NPSounds(PackOutput output, ExistingFileHelper helper) {
        super(output, NekoPlus.MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(
            NPSoundEvents.PARTICLE_STABILIZER_WORKING.unwrapKey().orElseThrow().location(),
            definition()
                .subtitle("subtitles.block.particle_stabilizer.working")
                .with(
                    sound(NekoPlus.location("block/particle_stabilizer/working"), SoundDefinition.SoundType.EVENT)
                        .attenuationDistance(10)
                )
        );
    }
}

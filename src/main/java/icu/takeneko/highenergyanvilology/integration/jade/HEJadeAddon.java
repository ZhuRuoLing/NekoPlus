package icu.takeneko.highenergyanvilology.integration.jade;

import icu.takeneko.highenergyanvilology.block.ParticleStabilizerBlock;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class HEJadeAddon implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(
            ParticleStabilizerDataProvider.INSTANCE,
            ParticleStabilizerBlockEntity.class
        );
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ParticleStabilizerDataProvider.INSTANCE, ParticleStabilizerBlock.class);
    }
}

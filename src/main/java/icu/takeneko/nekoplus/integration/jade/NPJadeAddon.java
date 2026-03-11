package icu.takeneko.nekoplus.integration.jade;

import icu.takeneko.nekoplus.block.ParticleStabilizerBlock;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class NPJadeAddon implements IWailaPlugin {
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

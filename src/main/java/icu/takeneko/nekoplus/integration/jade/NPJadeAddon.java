package icu.takeneko.nekoplus.integration.jade;

import icu.takeneko.nekoplus.block.ParticleStabilizerBlock;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import icu.takeneko.nekoplus.integration.jade.client.OverclockableComponentProvider;
import icu.takeneko.nekoplus.integration.jade.client.ParticleStabilizerComponentProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        registration.registerBlockDataProvider(
            OverclockableDataProvider.INSTANCE,
            BlockEntity.class
        );
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ParticleStabilizerComponentProvider.INSTANCE, ParticleStabilizerBlock.class);
        registration.registerBlockComponent(
            OverclockableComponentProvider.INSTANCE,
            Block.class
        );
    }
}

package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.dubhe.anvilcraft.client.renderer.blockentity.LaserBlockRenderer;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.StellarEngineBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.TardisBlockEntity;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.AnvilonEmitterBlockEntityRenderer;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.ParticleStabilizerBlockEntityRenderer;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.StellarEngineBlockEntityRenderer;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.TardisBlockEntityRenderer;

public class HEBlockEntities {
    public static final BlockEntityEntry<AnvilonEmitterBlockEntity> ANVILION_EMITTER = HEAnvilology.REGISTRATE
        .blockEntity("anvilon_emitter", AnvilonEmitterBlockEntity::new)
        .validBlock(HEBlocks.ANVILON_EMITTER_BLOCK::get)
        .renderer(() -> AnvilonEmitterBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<ParticleStabilizerBlockEntity> PARTICLE_STABILIZER = HEAnvilology.REGISTRATE
        .blockEntity("particle_stabilizer", ParticleStabilizerBlockEntity::new)
        .validBlock(HEBlocks.PARTICLE_STABILIZER)
        .renderer(() -> ParticleStabilizerBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<StellarEngineBlockEntity> STELLAR_ENGINE = HEAnvilology.REGISTRATE
        .blockEntity("stellar_engine", StellarEngineBlockEntity::new)
        .validBlock(HEBlocks.STELLAR_ENGINE)
        .renderer(() -> StellarEngineBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<TardisBlockEntity> TARDIS =  HEAnvilology.REGISTRATE
        .blockEntity("tardis", TardisBlockEntity::new)
        .validBlock(HEBlocks.TARDIS)
        .renderer(() -> TardisBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<HighEnergyLaserBlockEntity> HIGH_ENERGY_LASER = HEAnvilology.REGISTRATE
        .blockEntity("high_energy_laser", HighEnergyLaserBlockEntity::new)
        .validBlock(HEBlocks.HIGH_ENERGY_LASER)
        .renderer(() -> LaserBlockRenderer::new)
        .register();

    public static void setupRegistration() {
    }
}

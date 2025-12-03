package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.block.entity.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.client.renderer.blockentity.AnvilonEmitterBlockEntityRenderer;

public class HEBlockEntities {
    public static final BlockEntityEntry<AnvilonEmitterBlockEntity> ANVILION_EMITTER = HEAnvilology.REGISTRATE
        .blockEntity("anvilon_emitter", AnvilonEmitterBlockEntity::new)
        .validBlock(HEBlocks.ANVILON_EMITTER_BLOCK::get)
        .renderer(() -> AnvilonEmitterBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<ParticleStabilizerBlockEntity> PARTICLE_STABILIZER = HEAnvilology.REGISTRATE
        .blockEntity("particle_stabilizer", ParticleStabilizerBlockEntity::new)
        .validBlock(HEBlocks.PARTICLE_STABILIZER)
        .register();

    public static void setupRegistration() {
    }
}

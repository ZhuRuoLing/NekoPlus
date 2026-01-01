package icu.takeneko.highenergyanvilology.all;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.client.renderer.blockentity.LaserBlockRenderer;
import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.block.HEHatchBlock;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.HEHatchBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.StellarEngineBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.TardisBlockEntity;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.AnvilonEmitterBlockEntityRenderer;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.ParticleStabilizerBlockEntityRenderer;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.StellarEngineBlockEntityRenderer;
import icu.takeneko.highenergyanvilology.client.renderer.tesr.TardisBlockEntityRenderer;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.HEHatchTypes;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.HatchType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

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

    public static final BlockEntityEntry<TardisBlockEntity> TARDIS = HEAnvilology.REGISTRATE
        .blockEntity("tardis", TardisBlockEntity::new)
        .validBlock(HEBlocks.TARDIS)
        .renderer(() -> TardisBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<HighEnergyLaserBlockEntity> HIGH_ENERGY_LASER = HEAnvilology.REGISTRATE
        .blockEntity("high_energy_laser", HighEnergyLaserBlockEntity::new)
        .validBlock(HEBlocks.HIGH_ENERGY_LASER)
        .renderer(() -> LaserBlockRenderer::new)
        .register();

    public static BlockEntityEntry<HEHatchBlockEntity<IItemHandler>> ITEM_INPUT_HATCH = hatch(HEHatchTypes.ITEM, true, HEBlocks.ITEM_INPUT_HATCH);

    public static BlockEntityEntry<HEHatchBlockEntity<IItemHandler>> ITEM_OUTPUT_HATCH = hatch(HEHatchTypes.ITEM, false, HEBlocks.ITEM_OUTPUT_HATCH);

    public static BlockEntityEntry<HEHatchBlockEntity<IEnergyStorage>> ENERGY_OUTPUT_HATCH = hatch(HEHatchTypes.ENERGY, false, HEBlocks.ENERGY_OUTPUT_HATCH);

    public static <C> BlockEntityEntry<HEHatchBlockEntity<C>> hatch(
        HatchType<C> type,
        boolean isInput,
        BlockEntry<HEHatchBlock> blockEntry
    ) {
        String id = type.getSerializedName() + (isInput ? "_input" : "_output") + "_hatch";
        return HEAnvilology.REGISTRATE
            .<HEHatchBlockEntity<C>>blockEntity(
                id,
                (ty, pos, state) -> new HEHatchBlockEntity<>(ty, pos, state, type, isInput)
            )
            .validBlock(blockEntry)
            .register();
    }

    public static void setupRegistration() {
    }
}

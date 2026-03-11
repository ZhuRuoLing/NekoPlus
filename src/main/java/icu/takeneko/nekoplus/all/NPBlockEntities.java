package icu.takeneko.nekoplus.all;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.client.renderer.blockentity.LaserBlockRenderer;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.block.NPHatchBlock;
import icu.takeneko.nekoplus.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.nekoplus.block.tile.NPHatchBlockEntity;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.block.tile.StampingPlatformBlockEntity;
import icu.takeneko.nekoplus.block.tile.StellarEngineBlockEntity;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;
import icu.takeneko.nekoplus.client.renderer.tesr.AnvilonEmitterBlockEntityRenderer;
import icu.takeneko.nekoplus.client.renderer.tesr.ParticleStabilizerBlockEntityRenderer;
import icu.takeneko.nekoplus.client.renderer.tesr.StellarEngineBlockEntityRenderer;
import icu.takeneko.nekoplus.client.renderer.tesr.TardisBlockEntityRenderer;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.NPHatchTypes;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchType;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class NPBlockEntities {
    public static final BlockEntityEntry<AnvilonEmitterBlockEntity> ANVILION_EMITTER = NekoPlus.REGISTRATE
        .blockEntity("anvilon_emitter", AnvilonEmitterBlockEntity::new)
        .validBlock(NPBlocks.ANVILON_EMITTER_BLOCK::get)
        .renderer(() -> AnvilonEmitterBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<ParticleStabilizerBlockEntity> PARTICLE_STABILIZER = NekoPlus.REGISTRATE
        .blockEntity("particle_stabilizer", ParticleStabilizerBlockEntity::new)
        .validBlock(NPBlocks.PARTICLE_STABILIZER)
        .renderer(() -> ParticleStabilizerBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<StellarEngineBlockEntity> STELLAR_ENGINE = NekoPlus.REGISTRATE
        .blockEntity("stellar_engine", StellarEngineBlockEntity::new)
        .validBlock(NPBlocks.STELLAR_ENGINE)
        .renderer(() -> StellarEngineBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<TardisBlockEntity> TARDIS = NekoPlus.REGISTRATE
        .blockEntity("tardis", TardisBlockEntity::new)
        .validBlock(NPBlocks.TARDIS)
        .renderer(() -> TardisBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<HighEnergyLaserBlockEntity> HIGH_ENERGY_LASER = NekoPlus.REGISTRATE
        .blockEntity("high_energy_laser", HighEnergyLaserBlockEntity::new)
        .validBlock(NPBlocks.HIGH_ENERGY_LASER)
        .renderer(() -> LaserBlockRenderer::new)
        .register();

    public static final BlockEntityEntry<StampingPlatformBlockEntity> STAMPING_PLATFORM = NekoPlus.REGISTRATE
        .blockEntity("stamping_platform", StampingPlatformBlockEntity::new)
        .validBlock(ModBlocks.STAMPING_PLATFORM)
        .register();

    public static BlockEntityEntry<NPHatchBlockEntity<IItemHandler>> ITEM_INPUT_HATCH = hatch(NPHatchTypes.ITEM, true, NPBlocks.ITEM_INPUT_HATCH);

    public static BlockEntityEntry<NPHatchBlockEntity<IItemHandler>> ITEM_OUTPUT_HATCH = hatch(NPHatchTypes.ITEM, false, NPBlocks.ITEM_OUTPUT_HATCH);

    public static BlockEntityEntry<NPHatchBlockEntity<IEnergyStorage>> ENERGY_OUTPUT_HATCH = hatch(NPHatchTypes.ENERGY, false, NPBlocks.ENERGY_OUTPUT_HATCH);

    public static <C> BlockEntityEntry<NPHatchBlockEntity<C>> hatch(
        HatchType<C> type,
        boolean isInput,
        BlockEntry<NPHatchBlock> blockEntry
    ) {
        String id = type.getSerializedName() + (isInput ? "_input" : "_output") + "_hatch";
        return NekoPlus.REGISTRATE
            .<NPHatchBlockEntity<C>>blockEntity(
                id,
                (ty, pos, state) -> new NPHatchBlockEntity<>(ty, pos, state, type, isInput)
            )
            .validBlock(blockEntry)
            .register();
    }

    public static void setupRegistration() {
    }
}

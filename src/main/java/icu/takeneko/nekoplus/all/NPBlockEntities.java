package icu.takeneko.nekoplus.all;

import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntityEntry;
import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.client.renderer.blockentity.LaserBlockRenderer;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.block.NPHatchBlock;
import icu.takeneko.nekoplus.block.tile.NPHatchBlockEntity;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.block.tile.StampingPlatformBlockEntity;
import icu.takeneko.nekoplus.block.tile.StellarEngineBlockEntity;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.NPHatchTypes;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchType;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class NPBlockEntities {
//    public static final BlockEntityEntry<AnvilonEmitterBlockEntity> ANVILION_EMITTER = NekoPlus.REGISTRUM
//        .blockEntity("anvilon_emitter", AnvilonEmitterBlockEntity::new)
//        .validBlock(NPBlocks.ANVILON_EMITTER_BLOCK::get)
//        .renderer(() -> AnvilonEmitterBlockEntityRenderer::new)
//        .register();

    public static final BlockEntityEntry<ParticleStabilizerBlockEntity> PARTICLE_STABILIZER = NekoPlus.REGISTRUM
        .blockEntity("particle_stabilizer", ParticleStabilizerBlockEntity::new)
        .validBlock(NPBlocks.PARTICLE_STABILIZER)
        .register();

    public static final BlockEntityEntry<StellarEngineBlockEntity> STELLAR_ENGINE = NekoPlus.REGISTRUM
        .blockEntity("stellar_engine", StellarEngineBlockEntity::new)
        .validBlock(NPBlocks.STELLAR_ENGINE)
        .register();

    public static final BlockEntityEntry<ProgrammableLogicGateBlockEntity> PROGRAMMABLE_LOGIC_GATE = NekoPlus.REGISTRUM
        .blockEntity("programmable_logic_gate", ProgrammableLogicGateBlockEntity::new)
        .validBlock(NPBlocks.PROGRAMMABLE_LOGIC_GATE)
        .register();

    public static final BlockEntityEntry<TardisBlockEntity> TARDIS = NekoPlus.REGISTRUM
        .blockEntity("tardis", TardisBlockEntity::new)
        .validBlock(NPBlocks.TARDIS)
        .register();

    public static final BlockEntityEntry<HighEnergyLaserBlockEntity> HIGH_ENERGY_LASER = NekoPlus.REGISTRUM
        .blockEntity("high_energy_laser", HighEnergyLaserBlockEntity::new)
        .validBlock(NPBlocks.HIGH_ENERGY_LASER)
        .renderer(() -> LaserBlockRenderer::new)
        .register();

    public static final BlockEntityEntry<StampingPlatformBlockEntity> STAMPING_PLATFORM = NekoPlus.REGISTRUM
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
        return NekoPlus.REGISTRUM
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

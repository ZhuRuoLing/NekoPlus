package icu.takeneko.nekoplus.all;

import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntityEntry;
import dev.dubhe.anvilcraft.client.renderer.blockentity.LaserBlockEntityRenderer;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.block.tile.BatteryBlockEntity;
import icu.takeneko.nekoplus.block.tile.BlastCrystalBlockEntity;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.block.tile.ShulkerHatchBlockEntity;
import icu.takeneko.nekoplus.block.tile.StampingPlatformBlockEntity;
import icu.takeneko.nekoplus.block.tile.StellarEngineBlockEntity;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;
import icu.takeneko.nekoplus.block.tile.TestBlockEntity;
import icu.takeneko.nekoplus.client.renderer.tesr.StellarEngineRenderer;
import icu.takeneko.nekoplus.client.renderer.tesr.TardisRenderer;

public class NPBlockEntities {
//    public static final BlockEntityEntry<AnvilonEmitterBlockEntity> ANVILION_EMITTER = NekoPlus.REGISTRUM
//        .blockEntity("anvilon_emitter", AnvilonEmitterBlockEntity::new)
//        .validBlock(NPBlocks.ANVILON_EMITTER_BLOCK::get)
//        .renderer(() -> AnvilonEmitterBlockEntityRenderer::new)
//        .register();

    public static final BlockEntityEntry<ShulkerHatchBlockEntity> SHULKER_HATCH = NekoPlus.REGISTRUM
        .blockEntity("shulker_hatch", ShulkerHatchBlockEntity::new)
        .validBlock(NPBlocks.SHULKER_HATCH)
        .register();

    public static final BlockEntityEntry<ParticleStabilizerBlockEntity> PARTICLE_STABILIZER = NekoPlus.REGISTRUM
        .blockEntity("particle_stabilizer", ParticleStabilizerBlockEntity::new)
        .validBlock(NPBlocks.PARTICLE_STABILIZER)
        .register();

    public static final BlockEntityEntry<TestBlockEntity> TEST = NekoPlus.REGISTRUM
        .blockEntity("test", TestBlockEntity::new)
        .validBlock(NPBlocks.TEST)
        .register();

    public static final BlockEntityEntry<BatteryBlockEntity> BATTERY = NekoPlus.REGISTRUM
        .blockEntity("battery", BatteryBlockEntity::new)
        .validBlock(NPBlocks.BATTERY)
        .register();

    public static final BlockEntityEntry<StellarEngineBlockEntity> STELLAR_ENGINE = NekoPlus.REGISTRUM
        .blockEntity("stellar_engine", StellarEngineBlockEntity::new)
        .validBlock(NPBlocks.STELLAR_ENGINE)
        .renderer(() -> StellarEngineRenderer::new)
        .register();

    public static final BlockEntityEntry<ProgrammableLogicGateBlockEntity> PROGRAMMABLE_LOGIC_GATE = NekoPlus.REGISTRUM
        .blockEntity("programmable_logic_gate", ProgrammableLogicGateBlockEntity::new)
        .validBlock(NPBlocks.PROGRAMMABLE_LOGIC_GATE)
        .register();

    public static final BlockEntityEntry<TardisBlockEntity> TARDIS = NekoPlus.REGISTRUM
        .blockEntity("tardis", TardisBlockEntity::new)
        .validBlock(NPBlocks.TARDIS)
        .renderer(() -> TardisRenderer::new)
        .register();

    public static final BlockEntityEntry<HighEnergyLaserBlockEntity> HIGH_ENERGY_LASER = NekoPlus.REGISTRUM
        .blockEntity("high_energy_laser", HighEnergyLaserBlockEntity::new)
        .validBlock(NPBlocks.HIGH_ENERGY_LASER)
        .renderer(() -> LaserBlockEntityRenderer::new)
        .register();

    public static final BlockEntityEntry<StampingPlatformBlockEntity> STAMPING_PLATFORM = NekoPlus.REGISTRUM
        .blockEntity("stamping_platform", StampingPlatformBlockEntity::new)
        .validBlock(ModBlocks.STAMPING_PLATFORM)
        .register();

    public static final BlockEntityEntry<BlastCrystalBlockEntity> BLAST_CRYSTAL = NekoPlus.REGISTRUM
        .blockEntity("blast_crystal", BlastCrystalBlockEntity::new)
        .validBlocks(NPBlocks.BLAST_CRYSTAL, NPBlocks.CRACKED_BLAST_CRYSTAL, NPBlocks.DAMAGED_BLAST_CRYSTAL)
        .register();

    public static void setupRegistration() {
    }
}

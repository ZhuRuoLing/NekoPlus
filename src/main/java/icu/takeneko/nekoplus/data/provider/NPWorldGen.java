package icu.takeneko.nekoplus.data.provider;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPTags;
import icu.takeneko.nekoplus.worldgen.structure.WorkshopRuinStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.Map;

public class NPWorldGen {
    public static final ResourceKey<Structure> WORKSHOP_RUIN = createKey("workshop_ruin");

    public static void bootstrapStructures(BootstrapContext<Structure> bootstrapContext) {
        HolderGetter<Biome> holderLookup = bootstrapContext.lookup(Registries.BIOME);
        bootstrapContext.register(
            WORKSHOP_RUIN,
            new WorkshopRuinStructure(
                new Structure.StructureSettings(
                    holderLookup.getOrThrow(NPTags.Biomes.HAS_RUIN),
                    Map.of(),
                    GenerationStep.Decoration.SURFACE_STRUCTURES,
                    TerrainAdjustment.NONE
                )
            )
        );
    }

    public static void bootstrapStructureSets(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> holderLookup = context.lookup(Registries.STRUCTURE);
        context.register(
            createSetKey("workshop_ruin"),
            new StructureSet(
                holderLookup.getOrThrow(WORKSHOP_RUIN),
                new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 1919810)
            )
        );
    }

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, NekoPlus.location(name));
    }

    private static ResourceKey<StructureSet> createSetKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, NekoPlus.location(name));
    }
}

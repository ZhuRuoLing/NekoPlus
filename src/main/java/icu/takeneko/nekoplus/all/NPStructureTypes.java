package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.worldgen.structure.WorkshopRuinStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NPStructureTypes {
    public static final DeferredRegister<StructureType<?>> DR = DeferredRegister.create(
        Registries.STRUCTURE_TYPE,
        NekoPlus.MODID
    );

    public static final DeferredHolder<StructureType<?>, StructureType<?>> RUIN = DR.register(
        "workshop_ruin",
        () -> () -> WorkshopRuinStructure.CODEC
    );
}

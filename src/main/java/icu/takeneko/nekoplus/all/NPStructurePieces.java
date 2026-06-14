package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.worldgen.structure.WorkshopRuinPiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NPStructurePieces {
    public static final DeferredRegister<StructurePieceType> DR = DeferredRegister.create(
        Registries.STRUCTURE_PIECE,
        NekoPlus.MODID
    );

    public static final DeferredHolder<StructurePieceType, StructurePieceType> WORKSHOP_RUIN = DR.register(
        "workshop_ruin",
        () -> WorkshopRuinPiece::new
    );
}

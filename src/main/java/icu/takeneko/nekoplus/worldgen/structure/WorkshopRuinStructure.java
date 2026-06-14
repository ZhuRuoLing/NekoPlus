package icu.takeneko.nekoplus.worldgen.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class WorkshopRuinStructure extends Structure {
    public static final Identifier[] AVAILABLE_TEMPLATES = {
        NekoPlus.location("workshop_ruin/ruins_1")
    };

    public static final MapCodec<Structure> CODEC = RecordCodecBuilder.mapCodec(ins ->
        ins.group(
            settingsCodec(ins)
        ).apply(ins, WorkshopRuinStructure::new)
    );

    public WorkshopRuinStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        return onTopOfChunkCenter(
            context,
            Heightmap.Types.WORLD_SURFACE_WG,
            builder -> this.generatePieces(builder, context)
        );

    }

    private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
        Identifier location = AVAILABLE_TEMPLATES[0];
        BlockPos offset = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
        WorkshopRuinPiece piece = new WorkshopRuinPiece(
            context.structureTemplateManager(),
            location,
            WorkshopRuinPiece.makeSettings(),
            offset
        );
        builder.addPiece(piece);
        if (piece.isTooBigToFitInWorldGenRegion()) {
            BoundingBox bb = piece.getBoundingBox();
            int height = Structure.getMeanFirstOccupiedHeight(context, bb.minX(), bb.getXSpan(), bb.minZ(), bb.getZSpan());
            piece.adjustPositionHeight(height);
        }
    }

    @Override
    public StructureType<?> type() {
        return NPStructureTypes.RUIN.get();
    }
}

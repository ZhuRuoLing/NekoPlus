package icu.takeneko.nekoplus.worldgen.structure;

import icu.takeneko.nekoplus.all.NPStructurePieces;
import icu.takeneko.nekoplus.data.provider.NPLootTablesProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.ShipwreckPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;

public final class WorkshopRuinPiece extends TemplateStructurePiece {
    private boolean heightAdjusted = false;

    public static final Map<String, ResourceKey<LootTable>> LOOT_MARKERS = Map.of(
        "chest", NPLootTablesProvider.WORKSHOP_RUIN
    );

    public WorkshopRuinPiece(
        StructureTemplateManager structureTemplateManager,
        Identifier templateLocation,
        StructurePlaceSettings placeSettings,
        BlockPos position
    ) {
        super(
            NPStructurePieces.WORKSHOP_RUIN.get(),
            0,
            structureTemplateManager,
            templateLocation,
            templateLocation.toString(),
            placeSettings,
            position
        );
    }

    public WorkshopRuinPiece(
        StructurePieceSerializationContext structurePieceSerializationContext,
        CompoundTag compoundTag
    ) {
        super(
            NPStructurePieces.WORKSHOP_RUIN.get(),
            compoundTag,
            structurePieceSerializationContext.structureTemplateManager(),
            _ -> makeSettings()
        );
        this.heightAdjusted = compoundTag.getBooleanOr("height_adjusted", false);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("height_adjusted", this.heightAdjusted);
    }

    public static StructurePlaceSettings makeSettings() {
        return new StructurePlaceSettings()
            .setRotation(Rotation.NONE)
            .setMirror(Mirror.NONE)
            .setRotationPivot(BlockPos.ZERO)
            .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
    }

    @Override
    protected void handleDataMarker(
        String markerId,
        BlockPos position,
        ServerLevelAccessor level,
        RandomSource random,
        BoundingBox chunkBB
    ) {
        ResourceKey<LootTable> lootTable = LOOT_MARKERS.get(markerId);
        if (lootTable != null) {
            RandomizableContainer.setBlockEntityLootTable(
                level,
                random,
                position.below(),
                lootTable
            );
        }
    }

    @Override
    public void postProcess(
        WorldGenLevel level,
        StructureManager structureManager,
        ChunkGenerator generator,
        RandomSource random,
        BoundingBox chunkBB,
        ChunkPos chunkPos,
        BlockPos referencePos
    ) {
        if (!this.heightAdjusted && !this.isTooBigToFitInWorldGenRegion()) {
            int minY = level.getMaxY() + 1;
            int mean = 0;
            Vec3i templateSize = this.template.getSize();
            Heightmap.Types heightmapType = Heightmap.Types.OCEAN_FLOOR_WG;
            int baseSize = templateSize.getX() * templateSize.getZ();
            if (baseSize == 0) {
                mean = level.getHeight(heightmapType, this.templatePosition.getX(), this.templatePosition.getZ());
            } else {
                BlockPos corner = this.templatePosition.offset(templateSize.getX() - 1, 0, templateSize.getZ() - 1);

                for (BlockPos p : BlockPos.betweenClosed(this.templatePosition, corner)) {
                    int heightmap = level.getHeight(heightmapType, p.getX(), p.getZ());
                    mean += heightmap;
                    minY = Math.min(minY, heightmap);
                }

                mean /= baseSize;
            }

            this.adjustPositionHeight(mean);
            super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
            return;
        }
        super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
    }

    public boolean isTooBigToFitInWorldGenRegion() {
        Vec3i size = this.template.getSize();
        return size.getX() > 32 || size.getY() > 32;
    }

    public void adjustPositionHeight(int newHeight) {
        this.heightAdjusted = true;
        this.templatePosition = new BlockPos(this.templatePosition.getX(), newHeight, this.templatePosition.getZ());
    }
}

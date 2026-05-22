package icu.takeneko.nekoplus.integration.jade;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class ParticleStabilizerDataProvider implements IServerDataProvider<BlockAccessor> {
    public static final ParticleStabilizerDataProvider INSTANCE = new ParticleStabilizerDataProvider();
    public static final Identifier ID = NekoPlus.location("particle_stabilizer");

    @Override
    public Identifier getUid() {
        return ID;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof ParticleStabilizerBlockEntity blockEntity) {
            compoundTag.putInt("Cooldown", blockEntity.getCountdown());
        }
    }
}

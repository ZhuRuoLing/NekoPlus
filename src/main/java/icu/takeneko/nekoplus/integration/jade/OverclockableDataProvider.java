package icu.takeneko.nekoplus.integration.jade;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class OverclockableDataProvider implements IServerDataProvider<BlockAccessor> {
    public static final Identifier ID = NekoPlus.location("overclockable");
    public static final OverclockableDataProvider INSTANCE = new OverclockableDataProvider();

    @Override
    public Identifier getUid() {
        return ID;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (!(blockAccessor.getBlockEntity() instanceof NPOverclockablePowerConsumer powerConsumer)) return;
        compoundTag.putBoolean("OCEnabled", powerConsumer.isOverclockEnabled());
        compoundTag.putBoolean("InOCState", powerConsumer.isOverclockable());
        compoundTag.putInt("OCMax", powerConsumer.maxOverclockRatio());
        compoundTag.putInt("OCCurrent", powerConsumer.isOverclockable() ? powerConsumer.currentOverclockRatio() : 0);
    }
}

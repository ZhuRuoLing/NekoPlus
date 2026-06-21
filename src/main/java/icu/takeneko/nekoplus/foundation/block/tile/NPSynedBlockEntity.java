package icu.takeneko.nekoplus.foundation.block.tile;

import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.locks.ReentrantLock;

public abstract class NPSynedBlockEntity
    extends BlockEntity
    implements ISyncPersistRPCBlockEntity
{
    public NPSynedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);


    @Override
    public IManagedStorage getSyncStorage() {
        return syncStorage;
    }

    @Override
    public IManagedStorage getRootStorage() {
        return syncStorage;
    }
}

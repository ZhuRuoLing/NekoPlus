package icu.takeneko.highenergyanvilology.foundation.block.tile;

import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAsyncAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoPersistBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.locks.ReentrantLock;

public abstract class HESynedBlockEntity
    extends BlockEntity
    implements IAsyncAutoSyncBlockEntity, IAutoPersistBlockEntity, IManaged
{
    public HESynedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    private final ReentrantLock lock = new ReentrantLock();
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);


    @Override
    public IManagedStorage getSyncStorage() {
        return syncStorage;
    }

    @Override
    public void onChanged() {
        setChanged();
    }

    @Override
    public IManagedStorage getRootStorage() {
        return syncStorage;
    }
}

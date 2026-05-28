package icu.takeneko.nekoplus.foundation.energy;

import com.lowdragmc.lowdraglib2.syncdata.IContentChangeAware;
import lombok.Getter;
import lombok.Setter;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class NPEnergyStorage extends SimpleEnergyHandler implements IContentChangeAware {

    @Getter
    @Setter
    private Runnable onContentsChanged;

    public NPEnergyStorage(int capacity) {
        super(capacity);
    }

    public NPEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public NPEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        int oldValue = energy;
        int value = super.extract(amount, transaction);
        if (energy != oldValue && onContentsChanged != null) {
            onContentsChanged.run();
        }
        return value;
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        int oldValue = energy;
        int value = super.insert(amount, transaction);
        if (energy != oldValue && onContentsChanged != null) {
            onContentsChanged.run();
        }
        return value;
    }
}

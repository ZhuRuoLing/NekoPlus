package icu.takeneko.highenergyanvilology.foundation.energy;

import com.lowdragmc.lowdraglib2.syncdata.IContentChangeAware;
import lombok.Getter;
import lombok.Setter;
import net.neoforged.neoforge.energy.EnergyStorage;

public class HEEnergyStorage extends EnergyStorage implements IContentChangeAware {

    @Getter
    @Setter
    private Runnable onContentsChanged;

    public HEEnergyStorage(int capacity) {
        super(capacity);
    }

    public HEEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public HEEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public HEEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        int oldValue = energy;
        int value = super.extractEnergy(toExtract, simulate);
        if (energy != oldValue && onContentsChanged != null) {
            onContentsChanged.run();
        }
        return value;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        int oldValue = energy;
        int value = super.receiveEnergy(toReceive, simulate);
        if (energy != oldValue && onContentsChanged != null) {
            onContentsChanged.run();
        }
        return value;
    }
}

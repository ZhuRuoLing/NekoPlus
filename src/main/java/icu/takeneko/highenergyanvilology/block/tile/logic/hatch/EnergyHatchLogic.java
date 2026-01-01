package icu.takeneko.highenergyanvilology.block.tile.logic.hatch;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.highenergyanvilology.foundation.energy.HEEnergyStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyHatchLogic implements HatchLogic<IEnergyStorage> {

    public static final int CAPACITY = 700 * 2400 * 80;

    private final HEEnergyStorage energyStorage = new HEEnergyStorage(CAPACITY);

    @Override
    public void tick() {
    }

    @Override
    public void onRemoved() {
    }

    @Override
    public IEnergyStorage getCapabilityInstance() {
        return energyStorage;
    }

    @Override
    public ModularUI createUI() {
        return null;
    }

    @Override
    public void setOnContentsChanged(Runnable onContentChanged) {
        energyStorage.setOnContentsChanged(onContentChanged);
    }

    @Override
    public Runnable getOnContentsChanged() {
        return energyStorage.getOnContentsChanged();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put("Amount", energyStorage.serializeNBT(provider));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("Amount", IntTag.TAG_INT)) {
            energyStorage.deserializeNBT(provider, nbt.get("Amount"));
        }
    }
}

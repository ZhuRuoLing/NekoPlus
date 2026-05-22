package icu.takeneko.nekoplus.content.tile.logic.hatch;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.nekoplus.foundation.energy.NPEnergyStorage;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public class EnergyHatchLogic implements HatchLogic<EnergyHandler> {

    public static final int CAPACITY = 700 * 2400 * 800;

    private final NPEnergyStorage energyStorage = new NPEnergyStorage(CAPACITY);

    @Override
    public void tick() {
    }

    @Override
    public void onRemoved() {
    }

    @Override
    public EnergyHandler getCapabilityInstance() {
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
    public void serialize(ValueOutput output) {
        energyStorage.serialize(output);
    }

    @Override
    public void deserialize(ValueInput input) {
        energyStorage.deserialize(input);
    }
}

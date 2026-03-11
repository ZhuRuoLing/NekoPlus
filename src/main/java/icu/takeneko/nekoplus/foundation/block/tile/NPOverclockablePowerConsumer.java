package icu.takeneko.nekoplus.foundation.block.tile;

public interface NPOverclockablePowerConsumer extends Overclockable, NPPowerConsumer {

    int getBaseInputPower();

    int getOverclockedInputPower();

    @Override
    default int getInputPower() {
        return isOverclockable() ? getOverclockedInputPower() : getBaseInputPower();
    }
}

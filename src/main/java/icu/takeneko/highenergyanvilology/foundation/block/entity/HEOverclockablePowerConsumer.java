package icu.takeneko.highenergyanvilology.foundation.block.entity;

public interface HEOverclockablePowerConsumer extends Overclockable, HEPowerConsumer {

    int getBaseInputPower();

    int getOverclockedInputPower();

    @Override
    default int getInputPower() {
        return isOverclockable() ? getOverclockedInputPower() : getBaseInputPower();
    }
}

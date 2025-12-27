package icu.takeneko.highenergyanvilology.foundation.block.tile;

public interface Overclockable {
    void setEfficiency(int value);

    int getBaseOverclockCost();

    int maxOverclockRatio();

    default boolean isOverclockable() {
        return true;
    }
}

package icu.takeneko.nekoplus.foundation.block.tile;

public interface Overclockable {
    void setEfficiency(int value);

    int getBaseOverclockCost();

    int maxOverclockRatio();

    int currentOverclockRatio();

    default boolean isOverclockable() {
        return true;
    }

    default boolean isOverclockEnabled() {
        return true;
    }
}

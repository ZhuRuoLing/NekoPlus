package icu.takeneko.highenergyanvilology.foundation.block.tile.hatch;

import java.util.Locale;

public enum HEHatchTypes implements HatchType {
    ITEM, FLUID, ENERGY;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}

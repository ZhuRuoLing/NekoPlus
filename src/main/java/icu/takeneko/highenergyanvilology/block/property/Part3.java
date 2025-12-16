package icu.takeneko.highenergyanvilology.block.property;

import dev.dubhe.anvilcraft.block.state.ISimpleMultiPartBlockState;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum Part3 implements StringRepresentable, ISimpleMultiPartBlockState<Part3> {
    TOP(2), MIDDLE(1), BOTTOM(0);

    private final int offsetY;

    Part3(int offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public int getOffsetX() {
        return 0;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    @Override
    public int getOffsetZ() {
        return 0;
    }
}

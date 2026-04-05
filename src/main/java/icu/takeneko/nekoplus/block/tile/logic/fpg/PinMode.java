package icu.takeneko.nekoplus.block.tile.logic.fpg;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum PinMode implements StringRepresentable {
    DISABLE, INPUT, OUTPUT;

    public static final Codec<PinMode> CODEC = StringRepresentable.fromValues(PinMode::values);

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}

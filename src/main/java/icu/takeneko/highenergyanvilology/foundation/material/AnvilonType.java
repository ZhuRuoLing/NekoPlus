package icu.takeneko.highenergyanvilology.foundation.material;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public class AnvilonType {
    public enum Contained implements StringRepresentable {
        UNSTABLE, ENTANGLED;

        @Override
        public String getSerializedName() {
            return name();
        }

        public static final Codec<Contained> CODEC = StringRepresentable.fromEnum(Contained::values);
        public static final StreamCodec<? super ByteBuf, Contained> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> Contained.values()[i],
            Contained::ordinal
        );
    }
}

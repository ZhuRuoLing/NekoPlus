package icu.takeneko.highenergyanvilology.foundation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class HECodecs {
    public static final Codec<AABB> AABB_CODEC = RecordCodecBuilder.create(ins ->
        ins.group(
            Vec3.CODEC.fieldOf("from").forGetter(AABB::getMinPosition),
            Vec3.CODEC.fieldOf("to").forGetter(AABB::getMaxPosition)
        ).apply(ins, AABB::new)
    );

    public static final Codec<UUID> UUID_CODEC = Codec.LONG.listOf().flatXmap(
        it -> {
            if (it.size() != 2) {
                return DataResult.error(() -> "Incorrect list length: " + it.size());
            }
            return DataResult.success(new UUID(it.getFirst(), it.getLast()));
        },
        it -> DataResult.success(List.of(it.getMostSignificantBits(), it.getLeastSignificantBits()))
    );

    public static final StreamCodec<? super ByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.of(
        (buf, uuid) -> buf.writeLong(uuid.getMostSignificantBits()).writeLong(uuid.getLeastSignificantBits()),
        (buf) -> new UUID(buf.readLong(), buf.readLong())
    );
}

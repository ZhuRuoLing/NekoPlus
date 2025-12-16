package icu.takeneko.highenergyanvilology.foundation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HECodecs {
    public static final Codec<AABB> AABB_CODEC = RecordCodecBuilder.create(ins ->
        ins.group(
            Vec3.CODEC.fieldOf("from").forGetter(AABB::getMinPosition),
            Vec3.CODEC.fieldOf("to").forGetter(AABB::getMaxPosition)
        ).apply(ins, AABB::new)
    );
}

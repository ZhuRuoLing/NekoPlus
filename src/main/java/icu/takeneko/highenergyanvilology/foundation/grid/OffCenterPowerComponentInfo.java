package icu.takeneko.highenergyanvilology.foundation.grid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.api.power.PowerComponentType;
import icu.takeneko.highenergyanvilology.foundation.HECodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public record OffCenterPowerComponentInfo(
    BlockPos pos,
    int consumes,
    int produces,
    int stores,
    int capacity,
    AABB range,
    PowerComponentType type
) {
    public static final Codec<OffCenterPowerComponentInfo> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(o -> o.pos),
            Codec.INT.fieldOf("consumes").forGetter(o -> o.consumes),
            Codec.INT.fieldOf("produces").forGetter(o -> o.produces),
            Codec.INT.fieldOf("stores").forGetter(o -> o.stores),
            Codec.INT.fieldOf("capacity").forGetter(o -> o.capacity),
            HECodecs.AABB_CODEC.fieldOf("range").forGetter(o -> o.range),
            PowerComponentType.CODEC.fieldOf("type").forGetter(o -> o.type))
        .apply(ins, OffCenterPowerComponentInfo::new)
    );
}

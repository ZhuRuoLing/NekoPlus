package icu.takeneko.highenergyanvilology.internal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.api.power.PowerComponentInfo;
import dev.dubhe.anvilcraft.api.power.SimplePowerGrid;
import icu.takeneko.highenergyanvilology.foundation.grid.OffCenterPowerComponentInfo;
import net.minecraft.core.BlockPos;

import java.util.List;

public class SimplePowerGridInternals {

    public static final Codec<SimplePowerGrid> ORIGINAL_CODEC = RecordCodecBuilder.create(ins ->
        ins.group(
            Codec.INT.fieldOf("hash").forGetter(SimplePowerGrid::getId),
            Codec.STRING.fieldOf("level").forGetter(SimplePowerGrid::getLevel),
            BlockPos.CODEC.fieldOf("pos").forGetter(SimplePowerGrid::getPos),
            PowerComponentInfo.CODEC.listOf().fieldOf("powerComponentInfoList").forGetter(SimplePowerGrid::getPowerComponentInfoList),
            Codec.INT.fieldOf("generate").forGetter(SimplePowerGrid::getGenerate),
            Codec.INT.fieldOf("consume").forGetter(SimplePowerGrid::getConsume)
        ).apply(ins, SimplePowerGrid::new)
    );

    public static final Codec<SimplePowerGrid> CODEC = RecordCodecBuilder.create(ins ->
        ins.group(
            ORIGINAL_CODEC.fieldOf("original").forGetter(o -> o),
            OffCenterPowerComponentInfo.CODEC.listOf().fieldOf("offCentered").forGetter(o -> ((Access) o).getOffCenterComponents())
        ).apply(ins, SimplePowerGridInternals::create)
    );

    private static SimplePowerGrid create(SimplePowerGrid simplePowerGrid, List<OffCenterPowerComponentInfo> o) {
        ((Access)simplePowerGrid).setOffCenterComponents(o);
        return simplePowerGrid;
    }

    public interface Access {
        List<OffCenterPowerComponentInfo> getOffCenterComponents();
        void setOffCenterComponents(List<OffCenterPowerComponentInfo> value);
    }
}

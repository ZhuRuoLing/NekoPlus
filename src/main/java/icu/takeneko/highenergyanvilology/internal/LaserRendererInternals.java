package icu.takeneko.highenergyanvilology.internal;

import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;

import java.util.Set;

public class LaserRendererInternals {

    public static boolean hasPureHELaserSource(BaseLaserBlockEntity be) {
        if (!(be instanceof Access access)) return false;
        return access.hasPureHELaserSource();
    }

    public interface Access {
        boolean hasPureHELaserSource();

        void updateFromSource(BaseLaserBlockEntity blockEntity, boolean value, Set<BaseLaserBlockEntity> context);

        void setPureHELaserSourceDirect(boolean value);
    }

    public interface PacketAccess {
        boolean isPureHELaserSource();

        void setPureHELaserSource(boolean value);
    }
}

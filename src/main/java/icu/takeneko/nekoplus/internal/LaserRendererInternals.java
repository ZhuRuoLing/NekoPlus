package icu.takeneko.nekoplus.internal;

import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;

import java.util.Set;

public class LaserRendererInternals {

    public static boolean hasPureHELaserSource(BaseLaserBlockEntity be) {
        if (!(be instanceof Extension extension)) return false;
        return extension.hasPureHELaserSource();
    }

    public interface Extension {
        boolean hasPureHELaserSource();

        void updateFromSource(BaseLaserBlockEntity blockEntity, boolean value, Set<BaseLaserBlockEntity> context);

        void setPureHELaserSourceDirect(boolean value);

        void onTurnOff();
    }

    public interface PacketAccess {
        boolean isPureHELaserSource();

        void setPureHELaserSource(boolean value);
    }
}

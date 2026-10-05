package icu.takeneko.nekoplus.internal;

import dev.dubhe.anvilcraft.api.laser.ILaserComponentOwner;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import icu.takeneko.nekoplus.all.NPLaserComponents;
import icu.takeneko.nekoplus.content.tile.laser.PureHighEnergyLaserComponent;
import net.minecraft.core.BlockPos;

public class LaserRendererInternals {

    public static boolean hasPureHELaserSource(ILaserComponentOwner be) {
        PureHighEnergyLaserComponent component = be.getComponent(NPLaserComponents.PURE_HIGH_ENERGY);
        return component != null && component.pure();
    }

    public interface RenderStateAccess {
        boolean isPureHELaserSource();
    }

    public interface Extension {
        void onTurnOff();

        boolean targets(BlockPos pos);
    }

    public interface PacketAccess {
        boolean isPureHELaserSource();

        void setPureHELaserSource(boolean value);
    }
}

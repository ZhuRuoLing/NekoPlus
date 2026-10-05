package icu.takeneko.nekoplus.content.tile.laser;

import dev.dubhe.anvilcraft.api.laser.ILaserComponent;

public record PureHighEnergyLaserComponent(boolean pure) implements ILaserComponent {
    public static final PureHighEnergyLaserComponent PURE = new PureHighEnergyLaserComponent(true);
    public static final PureHighEnergyLaserComponent IMPURE = new PureHighEnergyLaserComponent(false);
}

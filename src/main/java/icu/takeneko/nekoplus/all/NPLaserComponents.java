package icu.takeneko.nekoplus.all;

import dev.dubhe.anvilcraft.api.laser.ILaserComponentType;
import icu.takeneko.nekoplus.content.tile.laser.PureHighEnergyLaserComponent;

import java.util.List;

public final class NPLaserComponents {
    public static final ILaserComponentType<PureHighEnergyLaserComponent, Boolean> PURE_HIGH_ENERGY =
        new ILaserComponentType<>() {
            @Override
            public PureHighEnergyLaserComponent createInstance(Boolean pure) {
                return pure ? PureHighEnergyLaserComponent.PURE : PureHighEnergyLaserComponent.IMPURE;
            }

            @Override
            public PureHighEnergyLaserComponent mergeIncoming(List<PureHighEnergyLaserComponent> components) {
                return components.stream().allMatch(PureHighEnergyLaserComponent::pure)
                    ? PureHighEnergyLaserComponent.PURE
                    : PureHighEnergyLaserComponent.IMPURE;
            }

            @Override
            public int priority() {
                return 0;
            }
        };

    private NPLaserComponents() {
    }
}

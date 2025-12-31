package icu.takeneko.highenergyanvilology.all;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.foundation.block.tile.HEPowerComponent;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class HECapabilities {
    public static final BlockCapability<HEPowerComponent, Void> POWER_COMPONENT = BlockCapability.createVoid(
        HEAnvilology.location("power_component"),
        HEPowerComponent.class
    );
}

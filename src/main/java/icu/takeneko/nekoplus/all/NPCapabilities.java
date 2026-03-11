package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.block.tile.NPPowerComponent;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class NPCapabilities {
    public static final BlockCapability<NPPowerComponent, Void> POWER_COMPONENT = BlockCapability.createVoid(
        NekoPlus.location("power_component"),
        NPPowerComponent.class
    );
}

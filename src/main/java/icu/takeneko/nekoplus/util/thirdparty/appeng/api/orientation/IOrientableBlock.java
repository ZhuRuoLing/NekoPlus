package icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Implemented on many of AEs blocks to control their orientation.
 */
public interface IOrientableBlock {
    IOrientationStrategy getOrientationStrategy();

    default BlockOrientation getOrientation(BlockState state) {
        var strategy = getOrientationStrategy();
        var facing = strategy.getFacing(state);
        var spin = strategy.getSpin(state);
        return BlockOrientation.get(facing, spin);
    }
}

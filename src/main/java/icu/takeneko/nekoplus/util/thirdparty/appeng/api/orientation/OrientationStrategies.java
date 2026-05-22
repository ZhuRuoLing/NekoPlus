/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Default implementations for {@link IOrientationStrategy}.
 */
public final class OrientationStrategies {
    private static final IOrientationStrategy none = new NoOrientationStrategy();
    private static final IOrientationStrategy horizontalFacing = new HorizontalFacingStrategy();
    private static final IOrientationStrategy facing = new FacingStrategy(BlockStateProperties.FACING);
    private static final IOrientationStrategy facingNoPlayerRotation = new FacingStrategy(BlockStateProperties.FACING, false);
    private static final IOrientationStrategy full = new FacingWithSpinStrategy();

    /**
     * The blocks orientation cannot be changed.
     */
    public static IOrientationStrategy none() {
        return none;
    }

    public static IOrientationStrategy horizontalFacing() {
        return horizontalFacing;
    }

    /**
     * Block can be oriented in 6 directions, but not swivel around that axis.
     */
    public static IOrientationStrategy facing() {
        return facing;
    }

    /**
     * Block can be oriented in 6 directions, but not swivel around that axis.
     */
    public static IOrientationStrategy facingNoPlayerRotation() {
        return facingNoPlayerRotation;
    }

    /**
     * Block can be oriented in 6 directions and then can also be swiveled around that axis in 90° increments.
     */
//    public static IOrientationStrategy full() {
//        return full;
//    }
}

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

import net.minecraft.core.Direction;

public enum RelativeSide {
    FRONT(Direction.NORTH),
    BACK(Direction.SOUTH),
    TOP(Direction.UP),
    BOTTOM(Direction.DOWN),
    LEFT(Direction.WEST),
    RIGHT(Direction.EAST);

    private static final RelativeSide[] BY_UNROTATED_SIDE = new RelativeSide[Direction.values().length];

    static {
        for (var side : values()) {
            BY_UNROTATED_SIDE[side.unrotatedSide.ordinal()] = side;
        }
    }

    private final Direction unrotatedSide;

    RelativeSide(Direction unrotatedSide) {
        this.unrotatedSide = unrotatedSide;
    }

    /**
     * Find the relative side on the given absolute side of a block, assuming its default orientation.
     */
    public static RelativeSide fromUnrotatedSide(Direction side) {
        return BY_UNROTATED_SIDE[side.ordinal()];
    }

    public Direction getUnrotatedSide() {
        return unrotatedSide;
    }
}

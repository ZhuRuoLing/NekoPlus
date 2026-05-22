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


import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Implements a strategy that allows blocks to be oriented using a single directional property. It doesn't allow up and
 * down, and uses the player facing instead in those cases.
 */
public class HorizontalFacingStrategy extends FacingStrategy {
    public static final HorizontalFacingStrategy INSTANCE = new HorizontalFacingStrategy();

    protected HorizontalFacingStrategy() {
        super(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
        return setFacing(state, context.getHorizontalDirection().getOpposite());
    }
}

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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Implements a strategy that allows blocks to be oriented using a single directional property.
 */
public class FacingStrategy implements IOrientationStrategy {
    private final EnumProperty<Direction> property;
    private final List<Property<?>> properties;
    private final boolean allowsPlayerRotation;

    protected FacingStrategy(EnumProperty<Direction> property) {
        this(property, true);
    }

    protected FacingStrategy(EnumProperty<Direction> property, boolean allowsPlayerRotation) {
        this.property = property;
        this.properties = Collections.singletonList(property);
        this.allowsPlayerRotation = allowsPlayerRotation;
    }

    @Override
    public Direction getFacing(BlockState state) {
        return state.getValue(property);
    }

    @Override
    public BlockState setFacing(BlockState state, Direction facing) {
        if (!property.getPossibleValues().contains(facing)) {
            return state;
        }
        return state.setValue(property, facing);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
        return setFacing(state, context.getClickedFace());
    }

    @Override
    public boolean allowsPlayerRotation() {
        return allowsPlayerRotation;
    }

    @Override
    public Collection<Property<?>> getProperties() {
        return properties;
    }
}
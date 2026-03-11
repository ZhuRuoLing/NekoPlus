package icu.takeneko.nekoplus.foundation.multiblock.builder;

import icu.takeneko.nekoplus.foundation.multiblock.MultiBlockDefinition;
import icu.takeneko.nekoplus.foundation.multiblock.MultiBlockDefinitionLayer;
import icu.takeneko.nekoplus.foundation.multiblock.MultiBlockDirectionalMatcher;
import icu.takeneko.nekoplus.foundation.multiblock.MultiBlockMatcher;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MultiBlockDefinitionBuilder {
    private final List<MultiBlockLayerBuilder> layers = new ArrayList<>();

    public MultiBlockDefinitionBuilder() {
    }

    public MultiBlockLayerBuilder beginLayer() {
        return new MultiBlockLayerBuilder(this);
    }

    public MultiBlockDefinitionBuilder addLayer(MultiBlockLayerBuilder layerBuilder) {
        layers.add(layerBuilder);
        return this;
    }

    public MultiBlockDefinition build() {
        List<MultiBlockDefinitionLayer> list = layers.stream().map(MultiBlockLayerBuilder::inflate)
            .toList();
        return new MultiBlockDefinition(list, list.size());
    }

    public MultiBlockDirectionalMatcher buildRotated() {
        List<MultiBlockDefinitionLayer> list = layers.stream().map(MultiBlockLayerBuilder::inflate)
            .toList();
        Map<Direction, MultiBlockMatcher> map = new EnumMap<>(Direction.class);

        return new MultiBlockDirectionalMatcher(map);
    }
}

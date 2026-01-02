package icu.takeneko.highenergyanvilology.foundation.multiblock.builder;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockDefinitionBuilder {
    private final List<MultiBlockLayerBuilder> layers = new ArrayList<>();

    public MultiBlockDefinitionBuilder() {
    }

    public MultiBlockLayerBuilder beginLayer() {
        return new MultiBlockLayerBuilder(this);
    }

    public MultiBlockDefinitionBuilder addLayer(MultiBlockLayerBuilder layerBuilder){
        layers.add(layerBuilder);
        return this;
    }
}

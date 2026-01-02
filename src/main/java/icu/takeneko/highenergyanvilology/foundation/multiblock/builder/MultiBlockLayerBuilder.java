package icu.takeneko.highenergyanvilology.foundation.multiblock.builder;

import icu.takeneko.highenergyanvilology.foundation.multiblock.prediction.BlockStatePrediction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiBlockLayerBuilder {
    private final MultiBlockDefinitionBuilder owner;
    @Getter
    private final List<String> lines = new ArrayList<>();
    @Getter
    private final Map<Character, BlockStatePrediction> statePredictionMap = new HashMap<>();

    public MultiBlockLayerBuilder(MultiBlockDefinitionBuilder owner) {
        this.owner = owner;
    }

    public MultiBlockLayerBuilder layout(String... content) {
        lines.addAll(Arrays.asList(content));
        return this;
    }

    public MultiBlockLayerBuilder define(char ch, BlockStatePrediction prediction) {
        statePredictionMap.put(ch, prediction);
        return this;
    }

    public MultiBlockLayerBuilder define(String ch, BlockStatePrediction prediction) {
        statePredictionMap.put(ch.charAt(0), prediction);
        return this;
    }

    public BlockStatePrediction[][] inflate() {
        int maxWidth = lines.stream().mapToInt(String::length).max().orElseThrow();
        BlockStatePrediction[][] predictionss = new BlockStatePrediction[lines.size()][];
        int ln = 0;
        for (String line : lines) {
            int lineIdx = ln++;
            if (line.length() != maxWidth) {
                throw new IllegalArgumentException("Pattern line length must align with %d, got %d!".formatted(maxWidth, line.length()));
            }
            BlockStatePrediction[] predictions = new BlockStatePrediction[maxWidth];
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                BlockStatePrediction prediction = statePredictionMap.get(ch);
                if (prediction == null) {
                    throw new IllegalArgumentException("Unresolved symbol `%s` at line %d column %d".formatted(ch, lineIdx, i));
                }
                predictions[i] = prediction;
            }
            predictionss[lineIdx] = predictions;
        }
        return predictionss;
    }

    public MultiBlockDefinitionBuilder endLayer() {
        return owner.addLayer(this);
    }
}

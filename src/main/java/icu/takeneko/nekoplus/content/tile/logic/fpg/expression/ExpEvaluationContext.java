package icu.takeneko.nekoplus.content.tile.logic.fpg.expression;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpEvaluationContext {
    private final Map<String, TriBoolean> values = new HashMap<>();

    public ExpEvaluationContext() {
    }

    public boolean get(String key) {
        TriBoolean value = values.get(key);
        if (value == null || value == TriBoolean.UNDEFINED) {
            throw new EvaluationException(Component.translatable("evaluator.undefined_symbol", key));
        }
        return value.asBoolean();
    }

    public List<String> getInputKeys() {
        return new ArrayList<>(values.keySet());
    }

    public ExpEvaluationContext put(String key, boolean value) {
        values.put(key, value ? TriBoolean.TRUE : TriBoolean.FALSE);
        return this;
    }

    public ExpEvaluationContext putUndefined(String key) {
        values.put(key, TriBoolean.UNDEFINED);
        return this;
    }

    enum TriBoolean {
        TRUE, FALSE, UNDEFINED;

        public boolean asBoolean() {
            return this == TRUE;
        }
    }
}

package icu.takeneko.nekoplus.content.expression;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
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

    public void put(String key, boolean value) {
        values.put(key, value ? TriBoolean.TRUE : TriBoolean.FALSE);
    }

    public void putUndefined(String key) {
        values.put(key, TriBoolean.UNDEFINED);
    }

    enum TriBoolean {
        TRUE, FALSE, UNDEFINED;

        public boolean asBoolean() {
            return this == TRUE;
        }
    }
}

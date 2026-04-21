package icu.takeneko.nekoplus.content.tile.logic.fpg;

import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ExpEvaluationContext;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ExpEvaluator;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.AstNode;

import java.util.List;

public class EvaluationCache {
    private List<String> cachedKeys = null;
    private boolean[] values;

    public void rebuild(ExpEvaluationContext context, List<AstNode> tree, String outputStateName) {
        cachedKeys = context.getInputKeys();
        values = new boolean[1 << cachedKeys.size()];
        if (cachedKeys.contains(outputStateName)) {
            throw new IllegalStateException("Input state values contains output state name");
        }

        int n = cachedKeys.size();
        for (int key = 0; key < values.length; key++) {
            ExpEvaluationContext evalContext = new ExpEvaluationContext();
            for (int i = 0; i < n; i++) {
                String varName = cachedKeys.get(i);
                boolean value = ((key >> (n - 1 - i)) & 1) == 1;
                evalContext.put(varName, value);
            }
            for (AstNode astNode : tree) {
                ExpEvaluator.evaluate(astNode, evalContext);
            }
            boolean result = evalContext.get(outputStateName);
            values[key] = result;
        }
    }

    public boolean getResult(ExpEvaluationContext context) {
        if (cachedKeys == null) {
            throw new IllegalStateException("Cache has not been built");
        }

        int key = 0;
        int n = cachedKeys.size();
        for (int i = 0; i < n; i++) {
            if (context.get(cachedKeys.get(i))) {
                key |= 1 << (n - 1 - i);
            }
        }
        return values[key];
    }
}

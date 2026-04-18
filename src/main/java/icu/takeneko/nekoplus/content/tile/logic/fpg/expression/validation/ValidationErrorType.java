package icu.takeneko.nekoplus.content.tile.logic.fpg.expression.validation;

import net.minecraft.network.chat.Component;

public sealed interface ValidationErrorType
    permits ValidationErrorType.UnknownSymbol, ValidationErrorType.Summary {

    Component formatComponent(String contextLine, String symbol, int totalCount, int line);

    final class UnknownSymbol implements ValidationErrorType {
        public static final UnknownSymbol INSTANCE = new UnknownSymbol();

        @Override
        public Component formatComponent(String contextLine, String symbol, int totalCount, int line) {
            return Component.translatable("evaluator.inspection.undefined_symbol", symbol, line, contextLine);
        }
    }

    final class Summary implements ValidationErrorType {
        public static final Summary INSTANCE = new Summary();

        @Override
        public Component formatComponent(String contextLine, String symbol, int totalCount, int line) {
            if (totalCount == 1) {
                return Component.translatable("evaluator.inspection.summary");
            }
            return Component.translatable("evaluator.inspection.summary_pl", totalCount);
        }
    }
}

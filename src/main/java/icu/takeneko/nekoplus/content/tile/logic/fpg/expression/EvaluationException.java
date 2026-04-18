package icu.takeneko.nekoplus.content.tile.logic.fpg.expression;

import net.minecraft.network.chat.Component;

public class EvaluationException extends RuntimeException {
    private final Component message;

    public EvaluationException(Component message) {
        this.message = message;
    }

    public Component getPrettyMessage() {
        return message;
    }
}

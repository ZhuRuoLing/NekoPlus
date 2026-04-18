package icu.takeneko.nekoplus.content.tile.logic.fpg.expression;

public record ExpToken(ExpTokenType type, String lexeme, int position) {

    @Override
    public String toString() {
        return String.format("ExpToken(%s, '%s', pos=%d)", type, lexeme, position);
    }
}

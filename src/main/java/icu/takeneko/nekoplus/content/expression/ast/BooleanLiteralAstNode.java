package icu.takeneko.nekoplus.content.expression.ast;


public record BooleanLiteralAstNode(boolean value) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitBooleanLiteral(this);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

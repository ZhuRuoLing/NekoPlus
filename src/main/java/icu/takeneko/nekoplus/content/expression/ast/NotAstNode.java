package icu.takeneko.nekoplus.content.expression.ast;


public record NotAstNode(AstNode operand) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitNotExpression(this);
    }

    @Override
    public String toString() {
        return "!" + operand;
    }
}

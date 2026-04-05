package icu.takeneko.nekoplus.content.expression.ast;


public record OrAstNode(AstNode left, AstNode right) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitOrExpression(this);
    }

    @Override
    public String toString() {
        return "(" + left + " | " + right + ")";
    }
}

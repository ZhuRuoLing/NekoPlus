package icu.takeneko.nekoplus.content.expression.ast;


public record AndAstNode(AstNode left, AstNode right) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitAndExpression(this);
    }

    @Override
    public String toString() {
        return "(" + left + " & " + right + ")";
    }
}

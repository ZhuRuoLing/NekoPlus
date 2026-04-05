package icu.takeneko.nekoplus.content.expression.ast;


public record NorAstNode(AstNode left, AstNode right) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitNorExpression(this);
    }

    @Override
    public String toString() {
        return "(" + left + " !| " + right + ")";
    }
}

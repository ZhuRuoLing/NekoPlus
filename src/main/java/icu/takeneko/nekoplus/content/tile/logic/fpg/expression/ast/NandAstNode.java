package icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast;


public record NandAstNode(AstNode left, AstNode right) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitNandExpression(this);
    }

    @Override
    public String toString() {
        return "(" + left + " !& " + right + ")";
    }
}

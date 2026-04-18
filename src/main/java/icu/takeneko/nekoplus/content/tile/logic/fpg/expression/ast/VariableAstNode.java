package icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast;


public record VariableAstNode(String name) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public String toString() {
        return name;
    }
}

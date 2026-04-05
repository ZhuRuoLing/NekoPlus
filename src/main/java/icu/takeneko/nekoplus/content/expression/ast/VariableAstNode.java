package icu.takeneko.nekoplus.content.expression.ast;


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

package icu.takeneko.nekoplus.content.expression.ast;


public record AssignmentAstNode(String target, AstNode value) implements AstNode {

    @Override
    public <T> T accept(AstNodeVisitor<T> visitor) {
        return visitor.visitAssignment(this);
    }

    @Override
    public String toString() {
        return target + " = " + value;
    }
}

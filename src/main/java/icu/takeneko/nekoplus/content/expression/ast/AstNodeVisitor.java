package icu.takeneko.nekoplus.content.expression.ast;

public interface AstNodeVisitor<T> {
    T visitBooleanLiteral(BooleanLiteralAstNode node);

    T visitVariable(VariableAstNode node);

    T visitNotExpression(NotAstNode node);

    T visitAndExpression(AndAstNode node);

    T visitOrExpression(OrAstNode node);

    T visitXorExpression(XorAstNode node);

    T visitNandExpression(NandAstNode node);

    T visitNorExpression(NorAstNode node);

    T visitAssignment(AssignmentAstNode node);
}

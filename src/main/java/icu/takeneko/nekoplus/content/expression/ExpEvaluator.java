package icu.takeneko.nekoplus.content.expression;

import icu.takeneko.nekoplus.content.expression.ast.*;


public class ExpEvaluator implements AstNodeVisitor<Boolean> {

    private final ExpEvaluationContext context;

    public ExpEvaluator(ExpEvaluationContext context) {
        this.context = context;
    }
    
    public static ExpEvaluationContext evaluate(AstNode node, ExpEvaluationContext inputContext) {
        ExpEvaluator evaluator = new ExpEvaluator(inputContext);
        evaluator.evaluate(node);
        return evaluator.getContext();
    }
    
    public boolean evaluate(AstNode node) {
        return node.accept(this);
    }
    
    public ExpEvaluationContext getContext() {
        return context;
    }

    @Override
    public Boolean visitBooleanLiteral(BooleanLiteralAstNode node) {
        return node.value();
    }

    @Override
    public Boolean visitVariable(VariableAstNode node) {
        return context.get(node.name());
    }

    @Override
    public Boolean visitNotExpression(NotAstNode node) {
        return !node.operand().accept(this);
    }

    @Override
    public Boolean visitAndExpression(AndAstNode node) {
        return node.left().accept(this) && node.right().accept(this);
    }

    @Override
    public Boolean visitOrExpression(OrAstNode node) {
        return node.left().accept(this) || node.right().accept(this);
    }

    @Override
    public Boolean visitXorExpression(XorAstNode node) {
        return node.left().accept(this) ^ node.right().accept(this);
    }

    @Override
    public Boolean visitNandExpression(NandAstNode node) {
        return !(node.left().accept(this) && node.right().accept(this));
    }

    @Override
    public Boolean visitNorExpression(NorAstNode node) {
        return !(node.left().accept(this) || node.right().accept(this));
    }

    @Override
    public Boolean visitAssignment(AssignmentAstNode node) {
        boolean value = node.value().accept(this);
        context.put(node.target(), value);
        return value;
    }
}

package icu.takeneko.nekoplus.content.tile.logic.fpg.expression.validation;

import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.AndAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.AssignmentAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.AstNodeVisitor;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.BooleanLiteralAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.NandAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.NorAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.NotAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.OrAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.VariableAstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.XorAstNode;
import lombok.Getter;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExpValidator implements AstNodeVisitor<Void> {
    private final Set<String> symbols;
    private String expression;
    private int line = 0;
    @Getter
    private final List<Component> validationResult = new ArrayList<>();

    public ExpValidator(Set<String> symbols) {
        this.symbols = symbols;
    }

    @Override
    public Void visitBooleanLiteral(BooleanLiteralAstNode node) {
        return null;
    }

    @Override
    public Void visitVariable(VariableAstNode node) {
        if (!symbols.contains(node.name())) {
            validationResult.add(ValidationErrorType.UnknownSymbol.INSTANCE.formatComponent(expression, node.name(), validationResult.size(), line));
        }
        return null;
    }

    @Override
    public Void visitNotExpression(NotAstNode node) {
        node.operand().accept(this);
        return null;
    }

    @Override
    public Void visitAndExpression(AndAstNode node) {
        node.left().accept(this);
        node.right().accept(this);
        return null;
    }

    @Override
    public Void visitOrExpression(OrAstNode node) {
        node.left().accept(this);
        node.right().accept(this);
        return null;
    }

    @Override
    public Void visitXorExpression(XorAstNode node) {
        node.left().accept(this);
        node.right().accept(this);
        return null;
    }

    @Override
    public Void visitNandExpression(NandAstNode node) {
        node.left().accept(this);
        node.right().accept(this);
        return null;
    }

    @Override
    public Void visitNorExpression(NorAstNode node) {
        node.left().accept(this);
        node.right().accept(this);
        return null;
    }

    @Override
    public Void visitAssignment(AssignmentAstNode node) {
        node.value().accept(this);
        this.symbols.add(node.target());
        return null;
    }

    public void nextLine(String s) {
        this.expression = s;
        line++;
    }

    public void addSummary() {
        Component formatted = ValidationErrorType.Summary.INSTANCE.formatComponent(expression, "", validationResult.size(), line);
        validationResult.add(Component.empty());
        validationResult.add(formatted);
    }
}

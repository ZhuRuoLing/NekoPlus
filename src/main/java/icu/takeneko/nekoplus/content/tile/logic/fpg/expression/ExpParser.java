package icu.takeneko.nekoplus.content.tile.logic.fpg.expression;

import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.*;
import lombok.Getter;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Locale;

public class ExpParser {

    private final List<ExpToken> tokens;
    private int position;

    public ExpParser(List<ExpToken> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public AstNode parse() {
        AstNode node = parseExpression();
        if (!isAtEnd()) {
            ExpToken peek = peek();
            throw new ParseException("Unexpected token: " + peek, Component.translatable("evaluator.inspection.unexpected_token", peek.lexeme()));
        }
        return node;
    }

    private AstNode parseExpression() {
        return parseAssignment();
    }

    private AstNode parseAssignment() {
        AstNode node = parseOr();

        if (match(ExpTokenType.ASSIGN)) {
            if (node instanceof VariableAstNode var) {
                AstNode value = parseAssignment();
                return new AssignmentAstNode(var.name(), value);
            } else {
                throw new ParseException("Expression is not assignable", Component.translatable("evaluator.inspection.not_assignable", new AstNodePrinter().print(node)));
            }
        }

        return node;
    }

    private AstNode parseOr() {
        AstNode left = parseXor();

        while (match(ExpTokenType.OR) || match(ExpTokenType.NOR)) {
            boolean isNor = previous().type() == ExpTokenType.NOR;
            AstNode right = parseXor();
            left = isNor ? new NorAstNode(left, right) : new OrAstNode(left, right);
        }

        return left;
    }

    private AstNode parseXor() {
        AstNode left = parseAnd();

        while (match(ExpTokenType.XOR)) {
            AstNode right = parseAnd();
            left = new XorAstNode(left, right);
        }

        return left;
    }

    private AstNode parseAnd() {
        AstNode left = parseUnary();

        while (match(ExpTokenType.AND) || match(ExpTokenType.NAND)) {
            boolean isNand = previous().type() == ExpTokenType.NAND;
            AstNode right = parseUnary();
            left = isNand ? new NandAstNode(left, right) : new AndAstNode(left, right);
        }

        return left;
    }

    private AstNode parseUnary() {
        if (match(ExpTokenType.NOT)) {
            AstNode operand = parseUnary();
            return new NotAstNode(operand);
        }

        return parsePrimary();
    }

    private AstNode parsePrimary() {
        if (match(ExpTokenType.TRUE)) {
            return new BooleanLiteralAstNode(true);
        }

        if (match(ExpTokenType.FALSE)) {
            return new BooleanLiteralAstNode(false);
        }

        if (match(ExpTokenType.VARIABLE)) {
            return new VariableAstNode(previous().lexeme());
        }

        if (match(ExpTokenType.LPAREN)) {
            AstNode node = parseExpression();
            consume(ExpTokenType.RPAREN, "Expected ')' after expression");
            return node;
        }

        ExpToken peek = peek();
        throw new ParseException("Unexpected token: " + peek, Component.translatable("evaluator.inspection.unexpected_token", peek.lexeme()));
    }

    private boolean match(ExpTokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(ExpTokenType type) {
        if (isAtEnd()) return false;
        return peek().type() == type;
    }

    private ExpToken advance() {
        if (!isAtEnd()) position++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type() == ExpTokenType.EOF;
    }

    private ExpToken peek() {
        return tokens.get(position);
    }

    private ExpToken previous() {
        return tokens.get(position - 1);
    }

    private ExpToken consume(ExpTokenType type, String message) {
        if (check(type)) return advance();
        ExpToken peek = peek();
        throw new ParseException(message + ", got: " + peek, Component.translatable("evaluator.inspection.expect_" + type.name().toLowerCase(Locale.ROOT), peek.lexeme()));
    }

    public static class ParseException extends RuntimeException {
        @Getter
        private final Component formattedMessage;

        public ParseException(String message, Component formattedMessage) {
            super(message);
            this.formattedMessage = formattedMessage;
        }
    }
}

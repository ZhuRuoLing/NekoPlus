package icu.takeneko.nekoplus.content.expression;

import icu.takeneko.nekoplus.content.expression.ast.*;

import java.util.List;

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
            throw new ParseException("Unexpected token: " + peek());
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
                throw new ParseException("Left side of assignment must be a variable");
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

        throw new ParseException("Unexpected token: " + peek());
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
        throw new ParseException(message + ", got: " + peek());
    }
    
    public static class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }
    }
}

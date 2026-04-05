package icu.takeneko.nekoplus.content.expression;

import java.util.ArrayList;
import java.util.List;


public class ExpLexer {

    private final String input;
    private int position;
    private final int length;

    public ExpLexer(String input) {
        this.input = input;
        this.position = 0;
        this.length = input.length();
    }

    public List<ExpToken> tokenize() {
        List<ExpToken> tokens = new ArrayList<>();
        ExpToken token;
        do {
            token = nextToken();
            tokens.add(token);
        } while (token.type() != ExpTokenType.EOF);
        return tokens;
    }

    public ExpToken nextToken() {
        skipWhitespace();

        if (isAtEnd()) {
            return new ExpToken(ExpTokenType.EOF, "", position);
        }
        int startPos = position;
        char ch = peek();
        if (isAlpha(ch)) {
            return readIdentifier(startPos);
        }
        advance();
        return switch (ch) {
            case '&' -> new ExpToken(ExpTokenType.AND, "&", startPos);
            case '|' -> new ExpToken(ExpTokenType.OR, "|", startPos);
            case '!' -> {
                if (match('&')) {
                    yield new ExpToken(ExpTokenType.NAND, "!&", startPos);
                }
                if (match('|')) {
                    yield new ExpToken(ExpTokenType.NOR, "!|", startPos);
                }
                yield new ExpToken(ExpTokenType.NOT, "!", startPos);
            }
            case '^' -> new ExpToken(ExpTokenType.XOR, "^", startPos);
            case '(' -> new ExpToken(ExpTokenType.LPAREN, "(", startPos);
            case ')' -> new ExpToken(ExpTokenType.RPAREN, ")", startPos);
            case '=' -> new ExpToken(ExpTokenType.ASSIGN, "=", startPos);
            default -> new ExpToken(ExpTokenType.UNKNOWN, String.valueOf(ch), startPos);
        };
    }

    private ExpToken readIdentifier(int startPos) {
        StringBuilder sb = new StringBuilder();
        sb.append(advance());

        while (!isAtEnd() && isAlphaNumeric(peek())) {
            sb.append(advance());
        }

        String text = sb.toString();
        ExpTokenType type = switch (text.toLowerCase()) {
            case "true" -> ExpTokenType.TRUE;
            case "false" -> ExpTokenType.FALSE;
            case "and" -> ExpTokenType.AND;
            case "or" -> ExpTokenType.OR;
            case "not" -> ExpTokenType.NOT;
            case "xor" -> ExpTokenType.XOR;
            case "nand" -> ExpTokenType.NAND;
            case "nor" -> ExpTokenType.NOR;
            default -> ExpTokenType.VARIABLE;
        };

        return new ExpToken(type, text, startPos);
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek())) {
            advance();
        }
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return input.charAt(position);
    }

    private char advance() {
        if (isAtEnd()) return '\0';
        return input.charAt(position++);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (peek() != expected) return false;
        position++;
        return true;
    }

    private boolean isAtEnd() {
        return position >= length;
    }

    private boolean isAlpha(char ch) {
        return Character.isLetter(ch) || ch == '_';
    }

    private boolean isAlphaNumeric(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }
}

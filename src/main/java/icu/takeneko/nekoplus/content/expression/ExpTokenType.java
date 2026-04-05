package icu.takeneko.nekoplus.content.expression;

public enum ExpTokenType {
    // Literals
    TRUE,
    FALSE,
    VARIABLE,

    // Operators
    AND,        // &, and
    OR,         // |, or
    NOT,        // !, not
    XOR,        // ^, xor
    NAND,       // !&, nand
    NOR,        // !|, nor

    // Parentheses
    LPAREN,     // (
    RPAREN,     // )

    // Special
    ASSIGN,     // =
    EOF,
    UNKNOWN
}

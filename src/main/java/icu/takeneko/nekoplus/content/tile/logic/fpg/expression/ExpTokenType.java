package icu.takeneko.nekoplus.content.tile.logic.fpg.expression;

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

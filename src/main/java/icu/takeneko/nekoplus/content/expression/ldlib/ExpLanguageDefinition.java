package icu.takeneko.nekoplus.content.expression.ldlib;

import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.ILanguageDefinition;
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.LanguageDefinition;
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.TokenType;
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.TokenTypes;

import java.util.List;
import java.util.Set;

public class ExpLanguageDefinition {
    public static TokenType OPERATOR = new TokenType("BooleanOp").setPattern("!&|!\\||&|\\||!|\\^|\\b(and|or|not|xor|nand|nor)\\b");
    public static TokenType ASSIGN = new TokenType("Assign").setPattern("=");
    public static TokenType PAREN = new TokenType("Parentheses").setPattern("\\(|\\)");

    public static final ILanguageDefinition INSTANCE = new LanguageDefinition(
        "BooleanExpression",
        List.of(
            TokenTypes.IDENTIFIER,
            ASSIGN,
            OPERATOR,
            PAREN
        ),
        Set.of("    ")
    );
}

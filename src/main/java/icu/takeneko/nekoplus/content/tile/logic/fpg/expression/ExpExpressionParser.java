package icu.takeneko.nekoplus.content.tile.logic.fpg.expression;

import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.AstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.AstNodePrinter;

import java.util.List;

public class ExpExpressionParser {

    public static AstNode parse(String input) {
        ExpLexer lexer = new ExpLexer(input);
        List<ExpToken> tokens = lexer.tokenize();
        ExpParser parser = new ExpParser(tokens);
        return parser.parse();
    }

    public static List<ExpToken> tokenize(String input) {
        ExpLexer lexer = new ExpLexer(input);
        return lexer.tokenize();
    }

    public static String prettyPrint(AstNode node) {
        AstNodePrinter printer = new AstNodePrinter();
        return printer.print(node);
    }

    public static String prettyPrintTree(AstNode node) {
        AstNodePrinter printer = new AstNodePrinter();
        return printer.printTree(node);
    }

    public static String parseAndPrint(String input) {
        return prettyPrint(parse(input));
    }

    public static String parseAndPrintTree(String input) {
        return prettyPrintTree(parse(input));
    }
}

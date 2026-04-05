package icu.takeneko.nekoplus.content.expression.ast;


public class AstNodePrinter implements AstNodeVisitor<String> {
    private static final String INDENT = "  ";
    private static final String BRANCH = "├── ";
    private static final String LAST_BRANCH = "└── ";
    private static final String VERTICAL = "│   ";
    private static final String EMPTY = "    ";

    public String print(AstNode node) {
        return node.accept(this);
    }
    
    public String printTree(AstNode node) {
        StringBuilder sb = new StringBuilder();
        printTree(node, sb, "", true);
        return sb.toString();
    }

    private void printTree(AstNode node, StringBuilder sb, String prefix, boolean isLast) {
        String branch = isLast ? LAST_BRANCH : BRANCH;

        switch (node) {
            case BooleanLiteralAstNode lit -> sb.append(prefix).append(branch).append("BooleanLiteralAstNode: ").append(lit.value()).append("\n");
            case VariableAstNode var -> sb.append(prefix).append(branch).append("VariableAstNode: ").append(var.name()).append("\n");
            case NotAstNode not -> {
                sb.append(prefix).append(branch).append("NotAstNode (!)\n");
                String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);
                printTree(not.operand(), sb, newPrefix, true);
            }
            case AndAstNode and -> {
                sb.append(prefix).append(branch).append("AndAstNode (&)\n");
                String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);
                printTree(and.left(), sb, newPrefix, false);
                printTree(and.right(), sb, newPrefix, true);
            }
            case OrAstNode or -> {
                sb.append(prefix).append(branch).append("OrAstNode (|)\n");
                String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);
                printTree(or.left(), sb, newPrefix, false);
                printTree(or.right(), sb, newPrefix, true);
            }
            case XorAstNode xor -> {
                sb.append(prefix).append(branch).append("XorAstNode (^)\n");
                String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);
                printTree(xor.left(), sb, newPrefix, false);
                printTree(xor.right(), sb, newPrefix, true);
            }
            case NandAstNode nand -> {
                sb.append(prefix).append(branch).append("NandAstNode (!&)\n");
                String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);
                printTree(nand.left(), sb, newPrefix, false);
                printTree(nand.right(), sb, newPrefix, true);
            }
            case NorAstNode nor -> {
                sb.append(prefix).append(branch).append("NorAstNode (!|)\n");
                String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);
                printTree(nor.left(), sb, newPrefix, false);
                printTree(nor.right(), sb, newPrefix, true);
            }
            case AssignmentAstNode assign -> {
                sb.append(prefix).append(branch).append("AssignmentAstNode (=)\n");
                String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);
                sb.append(newPrefix).append(BRANCH).append("Target: ").append(assign.target()).append("\n");
                printTree(assign.value(), sb, newPrefix, true);
            }
            default -> sb.append(prefix).append(branch).append("Unknown node").append("\n");
        }
    }

    @Override
    public String visitBooleanLiteral(BooleanLiteralAstNode node) {
        return String.valueOf(node.value());
    }

    @Override
    public String visitVariable(VariableAstNode node) {
        return node.name();
    }

    @Override
    public String visitNotExpression(NotAstNode node) {
        return "(! " + node.operand().accept(this) + ")";
    }

    @Override
    public String visitAndExpression(AndAstNode node) {
        return "(" + node.left().accept(this) + " & " + node.right().accept(this) + ")";
    }

    @Override
    public String visitOrExpression(OrAstNode node) {
        return "(" + node.left().accept(this) + " | " + node.right().accept(this) + ")";
    }

    @Override
    public String visitXorExpression(XorAstNode node) {
        return "(" + node.left().accept(this) + " ^ " + node.right().accept(this) + ")";
    }

    @Override
    public String visitNandExpression(NandAstNode node) {
        return "(" + node.left().accept(this) + " !& " + node.right().accept(this) + ")";
    }

    @Override
    public String visitNorExpression(NorAstNode node) {
        return "(" + node.left().accept(this) + " !| " + node.right().accept(this) + ")";
    }

    @Override
    public String visitAssignment(AssignmentAstNode node) {
        return node.target() + " = " + node.value().accept(this);
    }
}

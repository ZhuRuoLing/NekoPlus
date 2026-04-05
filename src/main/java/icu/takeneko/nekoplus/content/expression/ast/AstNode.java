package icu.takeneko.nekoplus.content.expression.ast;


public interface AstNode {
    
    <T> T accept(AstNodeVisitor<T> visitor);
}

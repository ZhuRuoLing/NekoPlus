package icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast;


public interface AstNode {
    
    <T> T accept(AstNodeVisitor<T> visitor);
}

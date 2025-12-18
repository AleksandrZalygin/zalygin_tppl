package com.pascal.ast;

import com.pascal.Token;

public class BinOp implements ASTNode {
    private final ASTNode left;
    private final Token operator;
    private final ASTNode right;

    public BinOp(ASTNode left, Token operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public ASTNode getRight() {
        return right;
    }
}

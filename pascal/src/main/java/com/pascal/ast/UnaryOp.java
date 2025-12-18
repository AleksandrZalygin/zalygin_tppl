package com.pascal.ast;

import com.pascal.Token;

public class UnaryOp implements ASTNode {
    private final Token operator;
    private final ASTNode operand;

    public UnaryOp(Token operator, ASTNode operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public Token getOperator() {
        return operator;
    }

    public ASTNode getOperand() {
        return operand;
    }
}

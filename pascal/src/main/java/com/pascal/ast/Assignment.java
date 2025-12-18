package com.pascal.ast;

public class Assignment implements ASTNode {
    private final Var variable;
    private final ASTNode expression;

    public Assignment(Var variable, ASTNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public Var getVariable() {
        return variable;
    }

    public ASTNode getExpression() {
        return expression;
    }
}

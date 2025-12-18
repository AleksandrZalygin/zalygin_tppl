package com.pascal.ast;

public class Program implements ASTNode {
    private final ComplexStatement complexStatement;

    public Program(ComplexStatement complexStatement) {
        this.complexStatement = complexStatement;
    }

    public ComplexStatement getComplexStatement() {
        return complexStatement;
    }
}

package com.pascal.ast;

import java.util.List;

public class ComplexStatement implements ASTNode {
    private final List<ASTNode> statements;

    public ComplexStatement(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }
}

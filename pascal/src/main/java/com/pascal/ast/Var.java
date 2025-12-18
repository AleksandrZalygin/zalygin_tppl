package com.pascal.ast;

import com.pascal.Token;

public class Var implements ASTNode {
    private final Token token;

    public Var(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
        return (String) token.getValue();
    }
}

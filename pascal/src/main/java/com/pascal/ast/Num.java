package com.pascal.ast;

import com.pascal.Token;

public class Num implements ASTNode {
    private final Token token;

    public Num(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public int getValue() {
        return (int) token.getValue();
    }
}

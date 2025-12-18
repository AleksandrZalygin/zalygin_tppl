package com.pascal;

import com.pascal.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
    }

    private void eat(TokenType tokenType) {
        if (currentToken.getType() == tokenType) {
            currentToken = lexer.getNextToken();
        } else {
            throw new RuntimeException("Expected " + tokenType + " but got " + currentToken.getType());
        }
    }

    public Program program() {
        ComplexStatement complexStatement = complexStatement();
        eat(TokenType.DOT);
        return new Program(complexStatement);
    }

    private ComplexStatement complexStatement() {
        eat(TokenType.BEGIN);
        List<ASTNode> statements = statementList();
        eat(TokenType.END);
        return new ComplexStatement(statements);
    }

    private List<ASTNode> statementList() {
        List<ASTNode> statements = new ArrayList<>();
        ASTNode stmt = statement();
        if (!(stmt instanceof NoOp)) {
            statements.add(stmt);
        }

        while (currentToken.getType() == TokenType.SEMI) {
            eat(TokenType.SEMI);
            stmt = statement();
            if (!(stmt instanceof NoOp)) {
                statements.add(stmt);
            }
        }

        return statements;
    }

    private ASTNode statement() {
        if (currentToken.getType() == TokenType.BEGIN) {
            return complexStatement();
        } else if (currentToken.getType() == TokenType.ID) {
            return assignment();
        } else {
            return empty();
        }
    }

    private Assignment assignment() {
        Var variable = variable();
        eat(TokenType.ASSIGN);
        ASTNode expression = expr();
        return new Assignment(variable, expression);
    }

    private Var variable() {
        Token token = currentToken;
        eat(TokenType.ID);
        return new Var(token);
    }

    private NoOp empty() {
        return new NoOp();
    }

    private ASTNode expr() {
        ASTNode node = term();

        while (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
            Token operator = currentToken;
            if (operator.getType() == TokenType.PLUS) {
                eat(TokenType.PLUS);
            } else {
                eat(TokenType.MINUS);
            }
            node = new BinOp(node, operator, term());
        }

        return node;
    }

    private ASTNode term() {
        ASTNode node = factor();

        while (currentToken.getType() == TokenType.MUL || currentToken.getType() == TokenType.DIV) {
            Token operator = currentToken;
            if (operator.getType() == TokenType.MUL) {
                eat(TokenType.MUL);
            } else {
                eat(TokenType.DIV);
            }
            node = new BinOp(node, operator, factor());
        }

        return node;
    }

    private ASTNode factor() {
        Token token = currentToken;

        if (token.getType() == TokenType.PLUS) {
            eat(TokenType.PLUS);
            return new UnaryOp(token, factor());
        } else if (token.getType() == TokenType.MINUS) {
            eat(TokenType.MINUS);
            return new UnaryOp(token, factor());
        } else if (token.getType() == TokenType.INTEGER) {
            eat(TokenType.INTEGER);
            return new Num(token);
        } else if (token.getType() == TokenType.LPAREN) {
            eat(TokenType.LPAREN);
            ASTNode node = expr();
            eat(TokenType.RPAREN);
            return node;
        } else if (token.getType() == TokenType.ID) {
            return variable();
        } else {
            throw new RuntimeException("Unexpected token: " + token);
        }
    }
}

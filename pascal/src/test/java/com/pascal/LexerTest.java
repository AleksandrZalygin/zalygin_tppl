package com.pascal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testInteger() {
        Lexer lexer = new Lexer("123");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.INTEGER, token.getType());
        assertEquals(123, token.getValue());
    }

    @Test
    public void testMultipleIntegers() {
        Lexer lexer = new Lexer("123 456");
        Token token1 = lexer.getNextToken();
        assertEquals(TokenType.INTEGER, token1.getType());
        assertEquals(123, token1.getValue());
        Token token2 = lexer.getNextToken();
        assertEquals(TokenType.INTEGER, token2.getType());
        assertEquals(456, token2.getValue());
    }

    @Test
    public void testPlus() {
        Lexer lexer = new Lexer("+");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.PLUS, token.getType());
        assertEquals("+", token.getValue());
    }

    @Test
    public void testMinus() {
        Lexer lexer = new Lexer("-");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.MINUS, token.getType());
        assertEquals("-", token.getValue());
    }

    @Test
    public void testMultiply() {
        Lexer lexer = new Lexer("*");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.MUL, token.getType());
        assertEquals("*", token.getValue());
    }

    @Test
    public void testDivide() {
        Lexer lexer = new Lexer("/");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.DIV, token.getType());
        assertEquals("/", token.getValue());
    }

    @Test
    public void testLParen() {
        Lexer lexer = new Lexer("(");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.LPAREN, token.getType());
        assertEquals("(", token.getValue());
    }

    @Test
    public void testRParen() {
        Lexer lexer = new Lexer(")");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.RPAREN, token.getType());
        assertEquals(")", token.getValue());
    }

    @Test
    public void testSemicolon() {
        Lexer lexer = new Lexer(";");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.SEMI, token.getType());
        assertEquals(";", token.getValue());
    }

    @Test
    public void testDot() {
        Lexer lexer = new Lexer(".");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.DOT, token.getType());
        assertEquals(".", token.getValue());
    }

    @Test
    public void testAssign() {
        Lexer lexer = new Lexer(":=");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.ASSIGN, token.getType());
        assertEquals(":=", token.getValue());
    }

    @Test
    public void testBegin() {
        Lexer lexer = new Lexer("BEGIN");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.BEGIN, token.getType());
        assertEquals("BEGIN", token.getValue());
    }

    @Test
    public void testBeginLowercase() {
        Lexer lexer = new Lexer("begin");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.BEGIN, token.getType());
        assertEquals("begin", token.getValue());
    }

    @Test
    public void testEnd() {
        Lexer lexer = new Lexer("END");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.END, token.getType());
        assertEquals("END", token.getValue());
    }

    @Test
    public void testEndLowercase() {
        Lexer lexer = new Lexer("end");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.END, token.getType());
        assertEquals("end", token.getValue());
    }

    @Test
    public void testIdentifier() {
        Lexer lexer = new Lexer("x");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.ID, token.getType());
        assertEquals("x", token.getValue());
    }

    @Test
    public void testIdentifierWithDigits() {
        Lexer lexer = new Lexer("var123");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.ID, token.getType());
        assertEquals("var123", token.getValue());
    }

    @Test
    public void testIdentifierWithUnderscore() {
        Lexer lexer = new Lexer("var_name");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.ID, token.getType());
        assertEquals("var_name", token.getValue());
    }

    @Test
    public void testEOF() {
        Lexer lexer = new Lexer("");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.EOF, token.getType());
        assertNull(token.getValue());
    }

    @Test
    public void testWhitespace() {
        Lexer lexer = new Lexer("   \t\n  ");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    public void testExpression() {
        Lexer lexer = new Lexer("2 + 3");
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.PLUS, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.EOF, lexer.getNextToken().getType());
    }

    @Test
    public void testComplexExpression() {
        Lexer lexer = new Lexer("(2 + 3) * 4");
        assertEquals(TokenType.LPAREN, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.PLUS, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.RPAREN, lexer.getNextToken().getType());
        assertEquals(TokenType.MUL, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.EOF, lexer.getNextToken().getType());
    }

    @Test
    public void testAssignment() {
        Lexer lexer = new Lexer("x := 5");
        assertEquals(TokenType.ID, lexer.getNextToken().getType());
        assertEquals(TokenType.ASSIGN, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.EOF, lexer.getNextToken().getType());
    }

    @Test
    public void testBeginEnd() {
        Lexer lexer = new Lexer("BEGIN END");
        assertEquals(TokenType.BEGIN, lexer.getNextToken().getType());
        assertEquals(TokenType.END, lexer.getNextToken().getType());
        assertEquals(TokenType.EOF, lexer.getNextToken().getType());
    }

    @Test
    public void testInvalidCharacter() {
        Lexer lexer = new Lexer("@");
        assertThrows(RuntimeException.class, () -> lexer.getNextToken());
    }

    @Test
    public void testMultipleStatements() {
        Lexer lexer = new Lexer("x := 1; y := 2");
        assertEquals(TokenType.ID, lexer.getNextToken().getType());
        assertEquals(TokenType.ASSIGN, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.SEMI, lexer.getNextToken().getType());
        assertEquals(TokenType.ID, lexer.getNextToken().getType());
        assertEquals(TokenType.ASSIGN, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.EOF, lexer.getNextToken().getType());
    }

    @Test
    public void testProgram() {
        Lexer lexer = new Lexer("BEGIN x := 5 END.");
        assertEquals(TokenType.BEGIN, lexer.getNextToken().getType());
        assertEquals(TokenType.ID, lexer.getNextToken().getType());
        assertEquals(TokenType.ASSIGN, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.END, lexer.getNextToken().getType());
        assertEquals(TokenType.DOT, lexer.getNextToken().getType());
        assertEquals(TokenType.EOF, lexer.getNextToken().getType());
    }

    @Test
    public void testColonWithoutEquals() {
        Lexer lexer = new Lexer(":");
        assertThrows(RuntimeException.class, () -> lexer.getNextToken());
    }

    @Test
    public void testZero() {
        Lexer lexer = new Lexer("0");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.INTEGER, token.getType());
        assertEquals(0, token.getValue());
    }

    @Test
    public void testLargeNumber() {
        Lexer lexer = new Lexer("999999");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.INTEGER, token.getType());
        assertEquals(999999, token.getValue());
    }

    @Test
    public void testMixedCase() {
        Lexer lexer = new Lexer("BeGiN EnD");
        assertEquals(TokenType.BEGIN, lexer.getNextToken().getType());
        assertEquals(TokenType.END, lexer.getNextToken().getType());
    }

    @Test
    public void testAssignWithSpaces() {
        Lexer lexer = new Lexer(": =");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.ASSIGN, token.getType());
        assertEquals(":=", token.getValue());
    }

    @Test
    public void testAssignWithManySpaces() {
        Lexer lexer = new Lexer(":    =");
        Token token = lexer.getNextToken();
        assertEquals(TokenType.ASSIGN, token.getType());
        assertEquals(":=", token.getValue());
    }

    @Test
    public void testAssignmentWithSpaces() {
        Lexer lexer = new Lexer("x : = 5");
        assertEquals(TokenType.ID, lexer.getNextToken().getType());
        assertEquals(TokenType.ASSIGN, lexer.getNextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.getNextToken().getType());
        assertEquals(TokenType.EOF, lexer.getNextToken().getType());
    }
}

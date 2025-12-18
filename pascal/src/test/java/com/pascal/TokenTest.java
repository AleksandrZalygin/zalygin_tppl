package com.pascal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    public void testTokenCreation() {
        Token token = new Token(TokenType.INTEGER, 42);
        assertEquals(TokenType.INTEGER, token.getType());
        assertEquals(42, token.getValue());
    }

    @Test
    public void testTokenToString() {
        Token token = new Token(TokenType.PLUS, "+");
        String str = token.toString();
        assertTrue(str.contains("PLUS"));
        assertTrue(str.contains("+"));
    }

    @Test
    public void testTokenEquals() {
        Token token1 = new Token(TokenType.INTEGER, 42);
        Token token2 = new Token(TokenType.INTEGER, 42);
        assertEquals(token1, token2);
    }

    @Test
    public void testTokenNotEquals() {
        Token token1 = new Token(TokenType.INTEGER, 42);
        Token token2 = new Token(TokenType.INTEGER, 43);
        assertNotEquals(token1, token2);
    }

    @Test
    public void testTokenNotEqualsDifferentType() {
        Token token1 = new Token(TokenType.INTEGER, 42);
        Token token2 = new Token(TokenType.PLUS, "+");
        assertNotEquals(token1, token2);
    }

    @Test
    public void testTokenEqualsNull() {
        Token token = new Token(TokenType.INTEGER, 42);
        assertNotEquals(token, null);
    }

    @Test
    public void testTokenEqualsSameObject() {
        Token token = new Token(TokenType.INTEGER, 42);
        assertEquals(token, token);
    }

    @Test
    public void testTokenEqualsDifferentClass() {
        Token token = new Token(TokenType.INTEGER, 42);
        assertNotEquals(token, "not a token");
    }

    @Test
    public void testTokenHashCode() {
        Token token1 = new Token(TokenType.INTEGER, 42);
        Token token2 = new Token(TokenType.INTEGER, 42);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    public void testTokenHashCodeDifferent() {
        Token token1 = new Token(TokenType.INTEGER, 42);
        Token token2 = new Token(TokenType.INTEGER, 43);
        assertNotEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    public void testTokenWithNullValue() {
        Token token = new Token(TokenType.EOF, null);
        assertEquals(TokenType.EOF, token.getType());
        assertNull(token.getValue());
    }

    @Test
    public void testTokenEqualsNullValue() {
        Token token1 = new Token(TokenType.EOF, null);
        Token token2 = new Token(TokenType.EOF, null);
        assertEquals(token1, token2);
    }

    @Test
    public void testTokenNotEqualsOneNullValue() {
        Token token1 = new Token(TokenType.INTEGER, 42);
        Token token2 = new Token(TokenType.INTEGER, null);
        assertNotEquals(token1, token2);
    }

    @Test
    public void testTokenHashCodeWithNull() {
        Token token = new Token(TokenType.EOF, null);
        assertNotNull(token.hashCode());
    }

    @Test
    public void testTokenWithStringValue() {
        Token token = new Token(TokenType.ID, "variable");
        assertEquals(TokenType.ID, token.getType());
        assertEquals("variable", token.getValue());
    }

    @Test
    public void testTokenTypeNull() {
        Token token = new Token(null, 42);
        assertNull(token.getType());
        assertEquals(42, token.getValue());
    }
}

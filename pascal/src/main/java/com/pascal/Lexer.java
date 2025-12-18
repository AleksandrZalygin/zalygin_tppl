package com.pascal;

public class Lexer {
    private final String text;
    private int pos;
    private Character currentChar;

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
        this.currentChar = text.length() > 0 ? text.charAt(0) : null;
    }

    private void advance() {
        pos++;
        if (pos >= text.length()) {
            currentChar = null;
        } else {
            currentChar = text.charAt(pos);
        }
    }

    private void skipWhitespace() {
        while (currentChar != null && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private int integer() {
        StringBuilder result = new StringBuilder();
        while (currentChar != null && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return Integer.parseInt(result.toString());
    }

    private String id() {
        StringBuilder result = new StringBuilder();
        while (currentChar != null && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    private Character peek() {
        int peekPos = pos + 1;
        if (peekPos >= text.length()) {
            return null;
        }
        return text.charAt(peekPos);
    }

    private boolean isAssignOperator() {
        int peekPos = pos + 1;
        while (peekPos < text.length() && Character.isWhitespace(text.charAt(peekPos))) {
            peekPos++;
        }
        return peekPos < text.length() && text.charAt(peekPos) == '=';
    }

    public Token getNextToken() {
        while (currentChar != null) {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (Character.isDigit(currentChar)) {
                return new Token(TokenType.INTEGER, integer());
            }

            if (Character.isLetter(currentChar)) {
                String idValue = id();
                if (idValue.equalsIgnoreCase("BEGIN")) {
                    return new Token(TokenType.BEGIN, idValue);
                } else if (idValue.equalsIgnoreCase("END")) {
                    return new Token(TokenType.END, idValue);
                } else {
                    return new Token(TokenType.ID, idValue);
                }
            }

            if (currentChar == ':' && isAssignOperator()) {
                advance();
                skipWhitespace();
                if (currentChar != null && currentChar == '=') {
                    advance();
                    return new Token(TokenType.ASSIGN, ":=");
                }
            }

            if (currentChar == ';') {
                advance();
                return new Token(TokenType.SEMI, ";");
            }

            if (currentChar == '+') {
                advance();
                return new Token(TokenType.PLUS, "+");
            }

            if (currentChar == '-') {
                advance();
                return new Token(TokenType.MINUS, "-");
            }

            if (currentChar == '*') {
                advance();
                return new Token(TokenType.MUL, "*");
            }

            if (currentChar == '/') {
                advance();
                return new Token(TokenType.DIV, "/");
            }

            if (currentChar == '(') {
                advance();
                return new Token(TokenType.LPAREN, "(");
            }

            if (currentChar == ')') {
                advance();
                return new Token(TokenType.RPAREN, ")");
            }

            if (currentChar == '.') {
                advance();
                return new Token(TokenType.DOT, ".");
            }

            throw new RuntimeException("Invalid character: " + currentChar);
        }

        return new Token(TokenType.EOF, null);
    }
}

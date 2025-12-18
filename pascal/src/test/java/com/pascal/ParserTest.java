package com.pascal;

import com.pascal.ast.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testEmptyProgram() {
        Lexer lexer = new Lexer("BEGIN END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(0, program.getComplexStatement().getStatements().size());
    }

    @Test
    public void testSimpleAssignment() {
        Lexer lexer = new Lexer("BEGIN x := 5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(1, program.getComplexStatement().getStatements().size());
        assertTrue(program.getComplexStatement().getStatements().get(0) instanceof Assignment);
    }

    @Test
    public void testMultipleAssignments() {
        Lexer lexer = new Lexer("BEGIN x := 1; y := 2 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(2, program.getComplexStatement().getStatements().size());
        assertTrue(program.getComplexStatement().getStatements().get(0) instanceof Assignment);
        assertTrue(program.getComplexStatement().getStatements().get(1) instanceof Assignment);
    }

    @Test
    public void testAssignmentWithExpression() {
        Lexer lexer = new Lexer("BEGIN x := 2 + 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof BinOp);
    }

    @Test
    public void testComplexExpression() {
        Lexer lexer = new Lexer("BEGIN x := 2 + 3 * 4 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof BinOp);
    }

    @Test
    public void testParentheses() {
        Lexer lexer = new Lexer("BEGIN x := (2 + 3) * 4 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof BinOp);
    }

    @Test
    public void testUnaryPlus() {
        Lexer lexer = new Lexer("BEGIN x := +5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof UnaryOp);
    }

    @Test
    public void testUnaryMinus() {
        Lexer lexer = new Lexer("BEGIN x := -5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof UnaryOp);
    }

    @Test
    public void testNestedComplexStatement() {
        Lexer lexer = new Lexer("BEGIN BEGIN x := 1 END END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(1, program.getComplexStatement().getStatements().size());
        assertTrue(program.getComplexStatement().getStatements().get(0) instanceof ComplexStatement);
    }

    @Test
    public void testVariableReference() {
        Lexer lexer = new Lexer("BEGIN x := 5; y := x END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(2, program.getComplexStatement().getStatements().size());
        Assignment assignment2 = (Assignment) program.getComplexStatement().getStatements().get(1);
        assertTrue(assignment2.getExpression() instanceof Var);
    }

    @Test
    public void testMissingDot() {
        Lexer lexer = new Lexer("BEGIN END");
        Parser parser = new Parser(lexer);
        assertThrows(RuntimeException.class, () -> parser.program());
    }

    @Test
    public void testMissingEnd() {
        Lexer lexer = new Lexer("BEGIN x := 5.");
        Parser parser = new Parser(lexer);
        assertThrows(RuntimeException.class, () -> parser.program());
    }

    @Test
    public void testInvalidAssignment() {
        Lexer lexer = new Lexer("BEGIN x = 5 END.");
        Parser parser = new Parser(lexer);
        assertThrows(RuntimeException.class, () -> parser.program());
    }

    @Test
    public void testMultipleOperations() {
        Lexer lexer = new Lexer("BEGIN x := 1 + 2 + 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof BinOp);
        BinOp binOp = (BinOp) assignment.getExpression();
        assertTrue(binOp.getLeft() instanceof BinOp);
    }

    @Test
    public void testDivision() {
        Lexer lexer = new Lexer("BEGIN x := 10 / 2 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof BinOp);
    }

    @Test
    public void testSubtraction() {
        Lexer lexer = new Lexer("BEGIN x := 10 - 5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof BinOp);
    }

    @Test
    public void testEmptyStatement() {
        Lexer lexer = new Lexer("BEGIN ; END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(0, program.getComplexStatement().getStatements().size());
    }

    @Test
    public void testTrailingSemicolon() {
        Lexer lexer = new Lexer("BEGIN x := 5; END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(1, program.getComplexStatement().getStatements().size());
        assertTrue(program.getComplexStatement().getStatements().get(0) instanceof Assignment);
    }

    @Test
    public void testComplexNested() {
        Lexer lexer = new Lexer("BEGIN x := 1; BEGIN y := 2 END; z := 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        assertEquals(3, program.getComplexStatement().getStatements().size());
    }

    @Test
    public void testUnaryInExpression() {
        Lexer lexer = new Lexer("BEGIN x := 5 + -3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
        Assignment assignment = (Assignment) program.getComplexStatement().getStatements().get(0);
        assertTrue(assignment.getExpression() instanceof BinOp);
        BinOp binOp = (BinOp) assignment.getExpression();
        assertTrue(binOp.getRight() instanceof UnaryOp);
    }

    @Test
    public void testNestedParentheses() {
        Lexer lexer = new Lexer("BEGIN x := ((1 + 2)) END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        assertNotNull(program);
    }

    @Test
    public void testUnexpectedToken() {
        Lexer lexer = new Lexer("BEGIN 5 END.");
        Parser parser = new Parser(lexer);
        assertThrows(RuntimeException.class, () -> parser.program());
    }

    @Test
    public void testMissingExpression() {
        Lexer lexer = new Lexer("BEGIN x := END.");
        Parser parser = new Parser(lexer);
        assertThrows(RuntimeException.class, () -> parser.program());
    }

    @Test
    public void testUnbalancedParentheses() {
        Lexer lexer = new Lexer("BEGIN x := (1 + 2 END.");
        Parser parser = new Parser(lexer);
        assertThrows(RuntimeException.class, () -> parser.program());
    }
}

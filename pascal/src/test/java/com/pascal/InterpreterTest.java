package com.pascal;

import com.pascal.ast.*;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    @Test
    public void testEmptyProgram() {
        Lexer lexer = new Lexer("BEGIN END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(0, variables.size());
    }

    @Test
    public void testSimpleAssignment() {
        Lexer lexer = new Lexer("BEGIN x := 5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(1, variables.size());
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testAddition() {
        Lexer lexer = new Lexer("BEGIN x := 2 + 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testSubtraction() {
        Lexer lexer = new Lexer("BEGIN x := 10 - 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(7.0, variables.get("x"));
    }

    @Test
    public void testMultiplication() {
        Lexer lexer = new Lexer("BEGIN x := 4 * 5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(20.0, variables.get("x"));
    }

    @Test
    public void testDivision() {
        Lexer lexer = new Lexer("BEGIN x := 10 / 2 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testOperatorPrecedence() {
        Lexer lexer = new Lexer("BEGIN x := 2 + 3 * 4 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(14.0, variables.get("x"));
    }

    @Test
    public void testParentheses() {
        Lexer lexer = new Lexer("BEGIN x := (2 + 3) * 4 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(20.0, variables.get("x"));
    }

    @Test
    public void testUnaryPlus() {
        Lexer lexer = new Lexer("BEGIN x := +5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testUnaryMinus() {
        Lexer lexer = new Lexer("BEGIN x := -5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(-5.0, variables.get("x"));
    }

    @Test
    public void testVariableReference() {
        Lexer lexer = new Lexer("BEGIN x := 5; y := x END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(5.0, variables.get("x"));
        assertEquals(5.0, variables.get("y"));
    }

    @Test
    public void testVariableInExpression() {
        Lexer lexer = new Lexer("BEGIN x := 5; y := x + 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(5.0, variables.get("x"));
        assertEquals(8.0, variables.get("y"));
    }

    @Test
    public void testVariableReassignment() {
        Lexer lexer = new Lexer("BEGIN x := 5; x := 10 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(10.0, variables.get("x"));
    }

    @Test
    public void testMultipleVariables() {
        Lexer lexer = new Lexer("BEGIN x := 1; y := 2; z := 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(3, variables.size());
        assertEquals(1.0, variables.get("x"));
        assertEquals(2.0, variables.get("y"));
        assertEquals(3.0, variables.get("z"));
    }

    @Test
    public void testComplexExpression() {
        Lexer lexer = new Lexer("BEGIN x := 2 + 3 * (2 + 3) END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(17.0, variables.get("x"));
    }

    @Test
    public void testNestedBlocks() {
        Lexer lexer = new Lexer("BEGIN x := 1; BEGIN y := 2 END; z := 3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(3, variables.size());
        assertEquals(1.0, variables.get("x"));
        assertEquals(2.0, variables.get("y"));
        assertEquals(3.0, variables.get("z"));
    }

    @Test
    public void testUndefinedVariable() {
        Lexer lexer = new Lexer("BEGIN x := y END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        assertThrows(RuntimeException.class, () -> interpreter.interpret(program));
    }

    @Test
    public void testZeroDivision() {
        Lexer lexer = new Lexer("BEGIN x := 1 / 0 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(Double.POSITIVE_INFINITY, variables.get("x"));
    }

    @Test
    public void testNegativeResult() {
        Lexer lexer = new Lexer("BEGIN x := 3 - 10 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(-7.0, variables.get("x"));
    }

    @Test
    public void testChainedOperations() {
        Lexer lexer = new Lexer("BEGIN x := 1 + 2 + 3 + 4 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(10.0, variables.get("x"));
    }

    @Test
    public void testUnaryInExpression() {
        Lexer lexer = new Lexer("BEGIN x := 5 + -3 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(2.0, variables.get("x"));
    }

    @Test
    public void testDoubleUnary() {
        Lexer lexer = new Lexer("BEGIN x := --5 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testSelfAssignment() {
        Lexer lexer = new Lexer("BEGIN x := 5; x := x END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testFloatingPointDivision() {
        Lexer lexer = new Lexer("BEGIN x := 5 / 2 END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(2.5, variables.get("x"));
    }

    @Test
    public void testExample1() {
        Lexer lexer = new Lexer("BEGIN END.");
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(0, variables.size());
    }

    @Test
    public void testExample2() {
        String code = "BEGIN\n" +
                      "    x:= 2 + 3 * (2 + 3);\n" +
                      "    y:= 2 / 2 - 2 + 3 * ((1 + 1) + (1 + 1))\n" +
                      "END.";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(17.0, variables.get("x"));
        assertEquals(11.0, variables.get("y"));
    }

    @Test
    public void testExample3() {
        String code = "BEGIN\n" +
                      "    y := 2;\n" +
                      "    BEGIN\n" +
                      "        a := 3;\n" +
                      "        a := a;\n" +
                      "        b := 10 + a + 10 * y / 4;\n" +
                      "        c := a - b\n" +
                      "    END;\n" +
                      "    x := 11\n" +
                      "END.";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        Program program = parser.program();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
        Map<String, Double> variables = interpreter.getVariables();
        assertEquals(2.0, variables.get("y"));
        assertEquals(3.0, variables.get("a"));
        assertEquals(18.0, variables.get("b"));
        assertEquals(-15.0, variables.get("c"));
        assertEquals(11.0, variables.get("x"));
    }
}

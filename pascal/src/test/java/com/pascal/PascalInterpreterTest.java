package com.pascal;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PascalInterpreterTest {

    @Test
    public void testExecuteEmptyProgram() {
        Map<String, Double> variables = PascalInterpreter.execute("BEGIN END.");
        assertEquals(0, variables.size());
    }

    @Test
    public void testExecuteSimpleAssignment() {
        Map<String, Double> variables = PascalInterpreter.execute("BEGIN x := 5 END.");
        assertEquals(1, variables.size());
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testExecuteExample1() {
        String code = "BEGIN\nEND.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(0, variables.size());
    }

    @Test
    public void testExecuteExample2() {
        String code = "BEGIN\n" +
                      "    x:= 2 + 3 * (2 + 3);\n" +
                      "        y:= 2 / 2 - 2 + 3 * ((1 + 1) + (1 + 1))\n" +
                      "END.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(2, variables.size());
        assertEquals(17.0, variables.get("x"));
        assertEquals(11.0, variables.get("y"));
    }

    @Test
    public void testExecuteExample3() {
        String code = "BEGIN\n" +
                      "    y: = 2;\n" +
                      "    BEGIN\n" +
                      "        a := 3;\n" +
                      "        a := a;\n" +
                      "        b := 10 + a + 10 * y / 4;\n" +
                      "        c := a - b\n" +
                      "    END;\n" +
                      "    x := 11;\n" +
                      "END.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(5, variables.size());
        assertEquals(2.0, variables.get("y"));
        assertEquals(3.0, variables.get("a"));
        assertEquals(18.0, variables.get("b"));
        assertEquals(-15.0, variables.get("c"));
        assertEquals(11.0, variables.get("x"));
    }

    @Test
    public void testExecuteWithSpacesInAssign() {
        String code = "BEGIN\n    x : = 5\nEND.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(1, variables.size());
        assertEquals(5.0, variables.get("x"));
    }

    @Test
    public void testExecuteComplexExpression() {
        String code = "BEGIN\n    result := 2 + 3 * 4 - 6 / 2\nEND.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(1, variables.size());
        assertEquals(11.0, variables.get("result"));
    }

    @Test
    public void testExecuteMultipleStatements() {
        String code = "BEGIN\n    a := 1;\n    b := 2;\n    c := a + b\nEND.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(3, variables.size());
        assertEquals(1.0, variables.get("a"));
        assertEquals(2.0, variables.get("b"));
        assertEquals(3.0, variables.get("c"));
    }

    @Test
    public void testExecuteNestedBlocks() {
        String code = "BEGIN\n    x := 1;\n    BEGIN\n        y := 2\n    END;\n    z := 3\nEND.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(3, variables.size());
        assertEquals(1.0, variables.get("x"));
        assertEquals(2.0, variables.get("y"));
        assertEquals(3.0, variables.get("z"));
    }

    @Test
    public void testExecuteUnaryOperators() {
        String code = "BEGIN\n    x := +5;\n    y := -10\nEND.";
        Map<String, Double> variables = PascalInterpreter.execute(code);
        assertEquals(2, variables.size());
        assertEquals(5.0, variables.get("x"));
        assertEquals(-10.0, variables.get("y"));
    }

    @Test
    public void testExecuteError() {
        assertThrows(RuntimeException.class, () -> {
            PascalInterpreter.execute("BEGIN x := y END.");
        });
    }

    @Test
    public void testExecuteSyntaxError() {
        assertThrows(RuntimeException.class, () -> {
            PascalInterpreter.execute("BEGIN x = 5 END.");
        });
    }

    @Test
    public void testExecuteLexerError() {
        assertThrows(RuntimeException.class, () -> {
            PascalInterpreter.execute("BEGIN x := @ END.");
        });
    }
}

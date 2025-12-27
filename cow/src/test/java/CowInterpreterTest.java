import org.example.CowInterpreter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CowInterpreterTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void setInput(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    private String getOutput() {
        return outContent.toString();
    }

    @Test
    public void testMoOIncrement() throws IOException {
        Files.write(Paths.get("test_increment.cow"), "MoO MoO MoO OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_increment.cow");
        interpreter.execute();
        assertEquals("3", getOutput());
        Files.deleteIfExists(Paths.get("test_increment.cow"));
        interpreter.close();
    }

    @Test
    public void testMOoDecrement() throws IOException {
        Files.write(Paths.get("test_decrement.cow"), "MoO MoO MoO MOo OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_decrement.cow");
        interpreter.execute();
        assertEquals("2", getOutput());
        Files.deleteIfExists(Paths.get("test_decrement.cow"));
        interpreter.close();
    }

    @Test
    public void testMoONextCell() throws IOException {
        Files.write(Paths.get("test_next.cow"), "MoO MoO MoO moO MoO MoO mOo OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_next.cow");
        interpreter.execute();
        assertEquals("3", getOutput());
        Files.deleteIfExists(Paths.get("test_next.cow"));
        interpreter.close();
    }

    @Test
    public void testMOoPrevCell() throws IOException {
        Files.write(Paths.get("test_prev.cow"), "MoO MoO MoO moO MoO MoO mOo OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_prev.cow");
        interpreter.execute();
        assertEquals("3", getOutput());
        Files.deleteIfExists(Paths.get("test_prev.cow"));
        interpreter.close();
    }

    @Test
    public void testMOoPrevCellAtZero() throws IOException {
        Files.write(Paths.get("test_prev_zero.cow"), "mOo OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_prev_zero.cow");
        interpreter.execute();
        assertEquals("0", getOutput());
        Files.deleteIfExists(Paths.get("test_prev_zero.cow"));
        interpreter.close();
    }

    @Test
    public void testOOOZero() throws IOException {
        Files.write(Paths.get("test_zero.cow"), "MoO MoO MoO OOO OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_zero.cow");
        interpreter.execute();
        assertEquals("0", getOutput());
        Files.deleteIfExists(Paths.get("test_zero.cow"));
        interpreter.close();
    }

    @Test
    public void testMMMRegisterCopyToRegister() throws IOException {
        Files.write(Paths.get("test_register.cow"), "MoO MoO MoO MMM moO MMM OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_register.cow");
        interpreter.execute();
        assertEquals("3", getOutput());
        Files.deleteIfExists(Paths.get("test_register.cow"));
        interpreter.close();
    }

    @Test
    public void testMMMRegisterCopyFromRegister() throws IOException {
        Files.write(Paths.get("test_register2.cow"), "MoO MoO MoO MMM OOO MMM OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_register2.cow");
        interpreter.execute();
        assertEquals("3", getOutput());
        Files.deleteIfExists(Paths.get("test_register2.cow"));
        interpreter.close();
    }

    @Test
    public void testSimpleLoop() throws IOException {
        Files.write(Paths.get("test_loop.cow"), "MoO MoO MoO MOO OOM MOo moo".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_loop.cow");
        interpreter.execute();
        assertEquals("321", getOutput());
        Files.deleteIfExists(Paths.get("test_loop.cow"));
        interpreter.close();
    }

    @Test
    public void testLoopSkipWhenZero() throws IOException {
        Files.write(Paths.get("test_loop_skip.cow"), "MOO MoO moo OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_loop_skip.cow");
        interpreter.execute();
        assertEquals("0", getOutput());
        Files.deleteIfExists(Paths.get("test_loop_skip.cow"));
        interpreter.close();
    }

    @Test
    public void testNestedLoops() throws IOException {
        Files.write(Paths.get("test_nested.cow"), "MoO MoO MOO OOM MOo moo".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_nested.cow");
        interpreter.execute();
        assertEquals("21", getOutput());
        Files.deleteIfExists(Paths.get("test_nested.cow"));
        interpreter.close();
    }

    @Test
    public void testMooOutputChar() throws IOException {
        Files.write(Paths.get("test_moo_output.cow"), "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO Moo".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_moo_output.cow");
        interpreter.execute();
        assertEquals("A", getOutput());
        Files.deleteIfExists(Paths.get("test_moo_output.cow"));
        interpreter.close();
    }

    @Test
    public void testMooInputChar() throws IOException {
        Files.write(Paths.get("test_moo_input.cow"), "Moo OOM".getBytes());
        setInput("A");
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_moo_input.cow");
        interpreter.execute();
        assertEquals("65", getOutput());
        Files.deleteIfExists(Paths.get("test_moo_input.cow"));
        interpreter.close();
    }

    @Test
    public void testOomInputInteger() throws IOException {
        Files.write(Paths.get("test_oom_input.cow"), "oom OOM".getBytes());
        setInput("42");
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_oom_input.cow");
        interpreter.execute();
        assertEquals("42", getOutput());
        Files.deleteIfExists(Paths.get("test_oom_input.cow"));
        interpreter.close();
    }

    @Test
    public void testOomInputInvalidInteger() throws IOException {
        Files.write(Paths.get("test_oom_invalid.cow"), "oom OOM".getBytes());
        setInput("abc");
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_oom_invalid.cow");
        interpreter.execute();
        assertEquals("0", getOutput());
        Files.deleteIfExists(Paths.get("test_oom_invalid.cow"));
        interpreter.close();
    }

    @Test
    public void testMOOExecuteInstructionSimple() throws IOException {
        Files.write(Paths.get("test_exec.cow"), "OOM MoO MoO MoO MoO MoO MoO moO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO mOo mOO mOo OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_exec.cow");
        interpreter.execute();
        assertEquals("07", getOutput());
        Files.deleteIfExists(Paths.get("test_exec.cow"));
        interpreter.close();
    }

    @Test
    public void testMOOExecuteInstructionInvalidIndex() throws IOException {
        Files.write(Paths.get("test_exec_invalid.cow"), "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO mOO OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_exec_invalid.cow");
        interpreter.execute();
        assertEquals("100", getOutput());
        Files.deleteIfExists(Paths.get("test_exec_invalid.cow"));
        interpreter.close();
    }

    @Test
    public void testMOOExecuteInstructionNegativeIndex() throws IOException {
        Files.write(Paths.get("test_exec_neg.cow"), "MOo mOO OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_exec_neg.cow");
        interpreter.execute();
        assertEquals("-1", getOutput());
        Files.deleteIfExists(Paths.get("test_exec_neg.cow"));
        interpreter.close();
    }

    @Test
    public void testMemoryExpansion() throws IOException {
        Files.write(Paths.get("test_memory.cow"), "moO moO moO moO moO MoO OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_memory.cow");
        interpreter.execute();
        assertEquals("1", getOutput());
        Files.deleteIfExists(Paths.get("test_memory.cow"));
        interpreter.close();
    }

    @Test
    public void testParsingIgnoresInvalidChars() throws IOException {
        Files.write(Paths.get("test_parse.cow"), "hello MoO world MoO invalid MoO OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_parse.cow");
        interpreter.execute();
        assertEquals("3", getOutput());
        Files.deleteIfExists(Paths.get("test_parse.cow"));
        interpreter.close();
    }

    @Test
    public void testEmptyProgram() throws IOException {
        Files.write(Paths.get("test_empty.cow"), "".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_empty.cow");
        interpreter.execute();
        assertEquals("", getOutput());
        Files.deleteIfExists(Paths.get("test_empty.cow"));
        interpreter.close();
    }

    @Test
    public void testHelloCow() throws IOException {
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("hello.cow");
        interpreter.execute();
        assertEquals("Hello, World!", getOutput());
        interpreter.close();
    }

    @Test
    public void testFibCow() throws IOException {
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("fib.cow");
        interpreter.execute();
        assertTrue(getOutput().contains("1, 1, 2, 3, 5, 8"));
        interpreter.close();
    }

    @Test(expected = IOException.class)
    public void testLoadNonExistentFile() throws IOException {
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("nonexistent.cow");
        interpreter.close();
    }

    @Test
    public void testMultipleExecutions() throws IOException {
        Files.write(Paths.get("test_multi.cow"), "MoO MoO OOM".getBytes());
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_multi.cow");
        interpreter.execute();
        String firstOutput = getOutput();
        outContent.reset();
        interpreter.execute();
        String secondOutput = getOutput();
        assertEquals(firstOutput, secondOutput);
        Files.deleteIfExists(Paths.get("test_multi.cow"));
        interpreter.close();
    }

    @Test
    public void testMooInputException() throws IOException {
        Files.write(Paths.get("test_moo_exception.cow"), "Moo OOM".getBytes());
        InputStream brokenInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Test exception");
            }
        };
        System.setIn(brokenInputStream);
        CowInterpreter interpreter = new CowInterpreter();
        interpreter.loadProgram("test_moo_exception.cow");
        interpreter.execute();
        assertEquals("0", getOutput());
        Files.deleteIfExists(Paths.get("test_moo_exception.cow"));
        interpreter.close();
    }

}

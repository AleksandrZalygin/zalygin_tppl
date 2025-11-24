package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.example.CowInterpreter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class CowInterpreterTest {

    private CowInterpreter interpreter;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
        interpreter = new CowInterpreter(inputStream, outputStream);
    }

    @Test
    void testDefaultConstructor() {
        CowInterpreter defaultInterpreter = new CowInterpreter();
        assertNotNull(defaultInterpreter);
        assertEquals(0, defaultInterpreter.getPointer());
        assertNull(defaultInterpreter.getRegister());
    }

    @Test
    void testParameterizedConstructor() {
        assertNotNull(interpreter);
        assertEquals(0, interpreter.getPointer());
        assertNull(interpreter.getRegister());
    }

    // ============================================
    // Тесты парсинга токенов
    // ============================================

    @Test
    void testParseTokens_EmptyString() {
        String[] tokens = interpreter.parseTokens("");
        assertEquals(0, tokens.length);
    }

    @Test
    void testParseTokens_SingleToken() {
        String[] tokens = interpreter.parseTokens("MoO");
        assertEquals(1, tokens.length);
        assertEquals("MoO", tokens[0]);
    }

    @Test
    void testParseTokens_MultipleTokens() {
        String[] tokens = interpreter.parseTokens("MoO MOo moO");
        assertEquals(3, tokens.length);
        assertEquals("MoO", tokens[0]);
        assertEquals("MOo", tokens[1]);
        assertEquals("moO", tokens[2]);
    }

    @Test
    void testParseTokens_AllValidTokens() {
        String source = "MoO MOo moO mOo moo MOO OOM oom mOO Moo OOO MMM";
        String[] tokens = interpreter.parseTokens(source);
        assertEquals(12, tokens.length);
    }

    @Test
    void testParseTokens_WithWhitespace() {
        String source = "MoO \n MOo \t moO";
        String[] tokens = interpreter.parseTokens(source);
        assertEquals(3, tokens.length);
    }

    @Test
    void testParseTokens_WithComments() {
        String source = "MoO this is comment MOo";
        String[] tokens = interpreter.parseTokens(source);
        assertEquals(2, tokens.length);
    }

    @Test
    void testParseTokens_WithInvalidCharacters() {
        String source = "MoO 123 !@# MOo";
        String[] tokens = interpreter.parseTokens(source);
        assertEquals(2, tokens.length);
    }

    // ============================================
    // Тесты построения карты циклов
    // ============================================

    @Test
    void testBuildLoopMap_NoLoops() {
        String[] tokens = {"MoO", "MOo", "moO"};
        Map<Integer, Integer> loops = interpreter.buildLoopMap(tokens);
        assertTrue(loops.isEmpty());
    }

    @Test
    void testBuildLoopMap_SingleLoop() {
        String[] tokens = {"MOO", "MoO", "moo"};
        Map<Integer, Integer> loops = interpreter.buildLoopMap(tokens);
        assertEquals(2, loops.size());
        assertEquals(2, loops.get(0));
        assertEquals(0, loops.get(2));
    }

    @Test
    void testBuildLoopMap_NestedLoops() {
        String[] tokens = {"MOO", "MOO", "moo", "moo"};
        Map<Integer, Integer> loops = interpreter.buildLoopMap(tokens);
        assertEquals(4, loops.size());
        assertEquals(2, loops.get(1));
        assertEquals(1, loops.get(2));
        assertEquals(3, loops.get(0));
        assertEquals(0, loops.get(3));
    }

    @Test
    void testBuildLoopMap_UnmatchedOpenLoop() {
        String[] tokens = {"MOO", "MoO"};
        Map<Integer, Integer> loops = interpreter.buildLoopMap(tokens);
        assertTrue(loops.isEmpty());
    }

    @Test
    void testBuildLoopMap_UnmatchedCloseLoop() {
        String[] tokens = {"MoO", "moo"};
        Map<Integer, Integer> loops = interpreter.buildLoopMap(tokens);
        assertTrue(loops.isEmpty());
    }

    // ============================================
    // Тесты операторов: MoO (инкремент)
    // ============================================

    @Test
    void testMoO_Increment() throws IOException {
        interpreter.execute("MoO");
        assertEquals(1, interpreter.getMemory()[0]);
    }

    @Test
    void testMoO_MultipleIncrements() throws IOException {
        interpreter.execute("MoO MoO MoO");
        assertEquals(3, interpreter.getMemory()[0]);
    }

    // ============================================
    // Тесты операторов: MOo (декремент)
    // ============================================

    @Test
    void testMOo_Decrement() throws IOException {
        interpreter.setMemoryCell(0, 5);
        interpreter.execute("MOo");
        assertEquals(4, interpreter.getMemory()[0]);
    }

    @Test
    void testMOo_MultipleDecrements() throws IOException {
        interpreter.setMemoryCell(0, 5);
        interpreter.execute("MOo MOo MOo");
        assertEquals(2, interpreter.getMemory()[0]);
    }

    // ============================================
    // Тесты операторов: moO (следующая ячейка)
    // ============================================

    @Test
    void testMoO_NextCell() throws IOException {
        interpreter.execute("moO");
        assertEquals(1, interpreter.getPointer());
    }

    @Test
    void testMoO_MultipleCellsForward() throws IOException {
        interpreter.execute("moO moO moO");
        assertEquals(3, interpreter.getPointer());
    }

    @Test
    void testMoO_WrapAround() throws IOException {
        interpreter.setPointer(29999);
        interpreter.execute("moO");
        assertEquals(0, interpreter.getPointer());
    }

    // ============================================
    // Тесты операторов: mOo (предыдущая ячейка)
    // ============================================

    @Test
    void testMOo_PreviousCell() throws IOException {
        interpreter.setPointer(5);
        interpreter.execute("mOo");
        assertEquals(4, interpreter.getPointer());
    }

    @Test
    void testMOo_MultipleCellsBackward() throws IOException {
        interpreter.setPointer(5);
        interpreter.execute("mOo mOo mOo");
        assertEquals(2, interpreter.getPointer());
    }

    @Test
    void testMOo_WrapAroundBackward() throws IOException {
        interpreter.setPointer(0);
        interpreter.execute("mOo");
        assertEquals(29999, interpreter.getPointer());
    }

    // ============================================
    // Тесты операторов: OOM (вывод)
    // ============================================

    @Test
    void testOOM_OutputSingleChar() throws IOException {
        interpreter.setMemoryCell(0, 65);
        interpreter.execute("OOM");
        assertEquals("65", outputStream.toString());
    }

    @Test
    void testOOM_OutputMultipleChars() throws IOException {
        interpreter.setMemoryCell(0, 72);
        interpreter.setMemoryCell(1, 105);
        interpreter.execute("OOM moO OOM");
        assertEquals("72105", outputStream.toString());
    }

    // ============================================
    // Тесты операторов: oom (ввод)
    // ============================================

    @Test
    void testOom_InputSingleChar() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{65}); // 'A'
        interpreter = new CowInterpreter(inputStream, outputStream);
        interpreter.execute("oom");
        assertEquals(65, interpreter.getMemory()[0]);
    }

    @Test
    void testOom_InputMultipleChars() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{65, 66}); // 'A', 'B'
        interpreter = new CowInterpreter(inputStream, outputStream);
        interpreter.execute("oom moO oom");
        assertEquals(65, interpreter.getMemory()[0]);
        assertEquals(66, interpreter.getMemory()[1]);
    }

    @Test
    void testOom_InputEndOfStream() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        interpreter = new CowInterpreter(inputStream, outputStream);
        interpreter.setMemoryCell(0, 5);
        interpreter.execute("oom");
        assertEquals(5, interpreter.getMemory()[0]); // значение не меняется
    }

    // ============================================
    // Тесты операторов: Moo (условный ввод/вывод)
    // ============================================

    @Test
    void testMoo_OutputWhenNonZero() throws IOException {
        interpreter.setMemoryCell(0, 65); // 'A'
        interpreter.execute("Moo");
        assertEquals("A", outputStream.toString());
    }

    @Test
    void testMoo_InputWhenZero() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{66}); // 'B'
        interpreter = new CowInterpreter(inputStream, outputStream);
        interpreter.execute("Moo");
        assertEquals(66, interpreter.getMemory()[0]);
    }

    @Test
    void testMoo_InputWhenZero_EndOfStream() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        interpreter = new CowInterpreter(inputStream, outputStream);
        interpreter.execute("Moo");
        assertEquals(0, interpreter.getMemory()[0]);
    }

    // ============================================
    // Тесты операторов: OOO (обнуление)
    // ============================================

    @Test
    void testOOO_ClearCell() throws IOException {
        interpreter.setMemoryCell(0, 42);
        interpreter.execute("OOO");
        assertEquals(0, interpreter.getMemory()[0]);
    }

    @Test
    void testOOO_ClearAlreadyZero() throws IOException {
        interpreter.execute("OOO");
        assertEquals(0, interpreter.getMemory()[0]);
    }

    // ============================================
    // Тесты операторов: MMM (регистр)
    // ============================================

    @Test
    void testMMM_CopyToRegister() throws IOException {
        interpreter.setMemoryCell(0, 42);
        interpreter.execute("MMM");
        assertEquals(42, interpreter.getRegister());
    }

    @Test
    void testMMM_CopyFromRegister() throws IOException {
        interpreter.setMemoryCell(0, 42);
        interpreter.execute("MMM moO MMM");
        assertEquals(0, interpreter.getMemory()[0]);
        assertEquals(42, interpreter.getMemory()[1]);
        assertNull(interpreter.getRegister());
    }

    @Test
    void testMMM_FullCycle() throws IOException {
        interpreter.setMemoryCell(0, 100);
        interpreter.execute("MMM moO MMM");
        assertEquals(100, interpreter.getMemory()[1]);
        assertNull(interpreter.getRegister());
    }

    // ============================================
    // Тесты операторов: moo/MOO (циклы)
    // ============================================

    @Test
    void testLoop_SkipWhenZero() throws IOException {
        interpreter.execute("MOO MoO moo");
        assertEquals(0, interpreter.getMemory()[0]);
    }

    @Test
    void testLoop_ExecuteWhenNonZero() throws IOException {
        interpreter.setMemoryCell(0, 3);
        interpreter.execute("MOO MOo moo");
        assertEquals(0, interpreter.getMemory()[0]);
    }

    @Test
    void testLoop_CountDown() throws IOException {
        interpreter.setMemoryCell(0, 5);
        interpreter.execute("MOO MOo moo");
        assertEquals(0, interpreter.getMemory()[0]);
    }

    @Test
    void testLoop_Nested() throws IOException {
        interpreter.setMemoryCell(0, 2);
        interpreter.setMemoryCell(1, 3);
        interpreter.execute("MOO moO MOO MOo moo mOo MOo moo");
        assertEquals(0, interpreter.getMemory()[0]);
        assertEquals(0, interpreter.getMemory()[1]);
    }

    // ============================================
    // Тесты операторов: mOO (переход)
    // ============================================

    @Test
    void testMOO_JumpToInstruction() throws IOException {
        interpreter.setMemoryCell(0, 2);
        interpreter.execute("mOO MoO MoO MoO");
        // Прыгает на инструкцию 2 (третью), пропуская две MoO
        assertEquals(1, interpreter.getMemory()[0]);
    }

    @Test
    void testMOO_JumpOutOfBounds() throws IOException {
        interpreter.setMemoryCell(0, 100);
        interpreter.execute("mOO MoO");
        // Прыжок за пределы, выполняется следующая инструкция
        assertEquals(101, interpreter.getMemory()[0]);
    }

    @Test
    void testMOO_JumpToNegative() throws IOException {
        interpreter.setMemoryCell(0, -1);
        interpreter.execute("mOO MoO");
        // Отрицательный прыжок игнорируется
        assertEquals(0, interpreter.getMemory()[0]);
    }

    @Test
    void testMOO_JumpToZero() throws IOException {
        interpreter.setMemoryCell(0, 0);
        interpreter.execute("mOO MoO");
        // Прыжок на начало - бесконечный цикл не происходит из-за -1
        assertEquals(1, interpreter.getMemory()[0]);
    }

    // ============================================
    // Комплексные тесты программ
    // ============================================

    @Test
    void testComplexProgram_HelloWorld() throws IOException {
        String program =
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MOO moO " +
                        "MoO MoO MoO MoO MoO MoO MoO Moo";
        interpreter.execute(program);
        String output = outputStream.toString();
        assertTrue(output.length() > 0);
    }

    @Test
    void testComplexProgram_IncrementAndOutput() throws IOException {
        // Увеличить до 72 ('H') и вывести
        StringBuilder program = new StringBuilder();
        for (int i = 0; i < 72; i++) {
            program.append("MoO ");
        }
        program.append("OOM");
        interpreter.execute(program.toString());
        assertEquals("H", outputStream.toString());
    }

    // ============================================
    // Тесты работы с файлами
    // ============================================

    @Test
    void testLoadSource(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.cow");
        Files.writeString(testFile, "MoO MOo moO");
        String source = CowInterpreter.loadSource(testFile);
        assertEquals("MoO MOo moO", source);
    }

    @Test
    void testExecuteFromFile(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.cow");
        String program = "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " +
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " +
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " +
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " +
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " +
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " +
                "MoO MoO MoO MoO MoO OOM"; // 65 = 'A'
        Files.writeString(testFile, program);

        interpreter.executeFromFile(testFile);
        assertEquals("A", outputStream.toString());
    }

    // ============================================
    // Тесты вспомогательных методов
    // ============================================

    @Test
    void testGetMemory() {
        interpreter.setMemoryCell(0, 42);
        interpreter.setMemoryCell(100, 99);
        int[] memory = interpreter.getMemory();
        assertEquals(42, memory[0]);
        assertEquals(99, memory[100]);
        // Проверяем, что возвращается копия
        memory[0] = 0;
        assertEquals(42, interpreter.getMemory()[0]);
    }

    @Test
    void testSetMemoryCell_Valid() {
        interpreter.setMemoryCell(10, 55);
        assertEquals(55, interpreter.getMemory()[10]);
    }

    @Test
    void testSetMemoryCell_NegativeIndex() {
        interpreter.setMemoryCell(-1, 55);
        // Не должно вызвать исключение, просто игнорируется
    }

    @Test
    void testSetMemoryCell_OutOfBounds() {
        interpreter.setMemoryCell(30000, 55);
        // Не должно вызвать исключение, просто игнорируется
    }

    @Test
    void testSetPointer() {
        interpreter.setPointer(100);
        assertEquals(100, interpreter.getPointer());
    }

    @Test
    void testReset() throws IOException {
        interpreter.setMemoryCell(0, 42);
        interpreter.setPointer(10);
        interpreter.execute("MMM");

        interpreter.reset();

        assertEquals(0, interpreter.getPointer());
        assertEquals(0, interpreter.getMemory()[0]);
        assertNull(interpreter.getRegister());
    }

    // ============================================
    // Интеграционные тесты с примерами
    // ============================================

    @Test
    void testHelloWorldProgram(@TempDir Path tempDir) throws IOException {
        Path helloFile = tempDir.resolve("hello.cow");
        String helloProgram =
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MOO moO MoO moO MoO MoO moO MoO MoO MoO " +
                        "moO MoO MoO MoO MoO moO MoO MoO MoO MoO MoO moO MoO MoO MoO MoO MoO MoO moO MoO " +
                        "MoO MoO MoO MoO MoO MoO moO MoO MoO MoO MoO MoO MoO MoO MoO moO MoO MoO MoO MoO " +
                        "MoO MoO MoO MoO MoO moO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO moO MoO MoO MoO " +
                        "MoO MoO MoO MoO MoO MoO MoO MoO moO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " +
                        "MoO moO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO mOo mOo mOo mOo mOo " +
                        "mOo mOo mOo mOo mOo mOo mOo mOo MOo moo moO moO moO moO moO moO moO moO MOo MOo " +
                        "MOo MOo MOo MOo MOo MOo Moo moO moO moO MOo MOo MOo MOo MOo MOo MOo MOo MOo Moo " +
                        "MoO MoO MoO MoO MoO MoO MoO Moo Moo moO MOo MOo MOo MOo MOo MOo MOo MOo MOo Moo " +
                        "mOo mOo mOo mOo mOo mOo mOo MOo MOo MOo MOo MOo MOo Moo mOo MOo MOo MOo MOo MOo " +
                        "MOo MOo MOo Moo moO moO moO moO moO MOo MOo MOo Moo moO moO moO Moo MoO MoO MoO " +
                        "Moo mOo Moo MOo MOo MOo MOo MOo MOo MOo MOo Moo mOo mOo mOo mOo mOo mOo mOo MoO " +
                        "Moo";
        Files.writeString(helloFile, helloProgram);

        interpreter.executeFromFile(helloFile);
        String output = outputStream.toString();
        assertTrue(output.contains("Hello"));
    }

    @Test
    void testFibonacciProgram_Partial(@TempDir Path tempDir) throws IOException {
        // Упрощенный тест части программы Фибоначчи
        String fibStart =
                "MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO MoO " + // 11 раз
                        "moO MoO " + // следующая ячейка, +1
                        "mOo mOo " + // назад
                        "OOM"; // вывод

        interpreter.execute(fibStart);
        String output = outputStream.toString();
        assertFalse(output.isEmpty());
    }

    @Test
    void testEmptyProgram() throws IOException {
        interpreter.execute("");
        assertEquals(0, interpreter.getPointer());
        assertEquals(0, interpreter.getMemory()[0]);
    }

    @Test
    void testOnlyComments() throws IOException {
        interpreter.execute("this is just text with no valid tokens");
        assertEquals(0, interpreter.getPointer());
    }

    @Test
    void testMemoryIsolation() throws IOException {
        // Проверяем, что операции в одной ячейке не влияют на другие
        interpreter.execute("MoO MoO MoO moO MoO MoO MoO MoO moO MoO");
        assertEquals(3, interpreter.getMemory()[0]);
        assertEquals(5, interpreter.getMemory()[1]);
        assertEquals(1, interpreter.getMemory()[2]);
    }
}
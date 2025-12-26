import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataWriterTest {
    private static final String TEST_FILE = "test_output.txt";

    public void testWriteSingleLine() throws IOException {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);
        writer.write("Test line 1");
        writer.close();

        List<String> lines = readFile(TEST_FILE);
        TestRunner.assertEquals(1, lines.size(), "Should have 1 line");
        TestRunner.assertEquals("Test line 1", lines.get(0), "Content should match");

        cleanupTestFile();
    }

    public void testWriteMultipleLines() throws IOException {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);
        writer.write("Line 1");
        writer.write("Line 2");
        writer.write("Line 3");
        writer.close();

        List<String> lines = readFile(TEST_FILE);
        TestRunner.assertEquals(3, lines.size(), "Should have 3 lines");
        TestRunner.assertEquals("Line 1", lines.get(0), "Line 1 should match");
        TestRunner.assertEquals("Line 2", lines.get(1), "Line 2 should match");
        TestRunner.assertEquals("Line 3", lines.get(2), "Line 3 should match");

        cleanupTestFile();
    }

    public void testAppendMode() throws IOException {
        cleanupTestFile();

        DataWriter writer1 = new DataWriter(TEST_FILE);
        writer1.write("First session");
        writer1.close();

        DataWriter writer2 = new DataWriter(TEST_FILE);
        writer2.write("Second session");
        writer2.close();

        List<String> lines = readFile(TEST_FILE);
        TestRunner.assertEquals(2, lines.size(), "Should have 2 lines");
        TestRunner.assertEquals("First session", lines.get(0), "First line preserved");
        TestRunner.assertEquals("Second session", lines.get(1), "Second line appended");

        cleanupTestFile();
    }

    public void testThreadSafety() throws IOException, InterruptedException {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                writer.write("Thread1-" + i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                writer.write("Thread2-" + i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        writer.close();

        List<String> lines = readFile(TEST_FILE);
        TestRunner.assertEquals(200, lines.size(), "Should have 200 lines");

        cleanupTestFile();
    }

    public void testEmptyWrite() throws IOException {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);
        writer.write("");
        writer.close();

        List<String> lines = readFile(TEST_FILE);
        TestRunner.assertEquals(1, lines.size(), "Should have 1 line");
        TestRunner.assertEquals("", lines.get(0), "Should be empty string");

        cleanupTestFile();
    }

    public void testSpecialCharacters() throws IOException {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);
        writer.write("Special: !@#$%^&*()");
        writer.write("Unicode: привет мир");
        writer.close();

        List<String> lines = readFile(TEST_FILE);
        TestRunner.assertEquals(2, lines.size(), "Should have 2 lines");
        TestRunner.assertTrue(lines.get(0).contains("!@#$%^&*()"), "Should contain special chars");

        cleanupTestFile();
    }

    private List<String> readFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private void cleanupTestFile() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}

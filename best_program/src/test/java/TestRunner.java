import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    private static int passed = 0;
    private static int failed = 0;
    private static List<String> failures = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Running tests...\n");

        runTestClass(new ServerConfigTest());
        runTestClass(new DataWriterTest());
        runTestClass(new DataCollectorTest());
        runTestClass(new DataCollectorIntegrationTest());

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Test Results:");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            System.out.println("\nFailures:");
            for (String failure : failures) {
                System.out.println("  - " + failure);
            }
            System.exit(1);
        } else {
            System.out.println("\nAll tests passed!");
            System.exit(0);
        }
    }

    private static void runTestClass(Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        System.out.println("Running " + testClass.getSimpleName() + ":");

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                try {
                    method.invoke(testInstance);
                    passed++;
                    System.out.println("  ✓ " + method.getName());
                } catch (Exception e) {
                    failed++;
                    String failure = testClass.getSimpleName() + "." + method.getName();
                    failures.add(failure + ": " + e.getCause().getMessage());
                    System.out.println("  ✗ " + method.getName() + " - " + e.getCause().getMessage());
                }
            }
        }
        System.out.println();
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }
}

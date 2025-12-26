import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String outputFile = "data.txt";

        DataWriter writer = new DataWriter(outputFile);

        ServerConfig server1 = new ServerConfig("95.163.237.76", 5123, 15);
        ServerConfig server2 = new ServerConfig("95.163.237.76", 5124, 21);

        DataCollector collector1 = new DataCollector(server1, writer, "Server1");
        DataCollector collector2 = new DataCollector(server2, writer, "Server2");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(collector1);
        executor.submit(collector2);

        System.out.println("Data collection started. Press Ctrl+C to stop.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down...");
            collector1.stop();
            collector2.stop();
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            writer.close();
            System.out.println("Shutdown complete.");
        }));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

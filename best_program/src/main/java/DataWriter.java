import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {
    private final BufferedWriter writer;
    private final Object lock = new Object();

    public DataWriter(String filename) {
        try {
            this.writer = new BufferedWriter(new FileWriter(filename, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to open file: " + filename, e);
        }
    }

    public void write(String data) {
        synchronized (lock) {
            try {
                writer.write(data);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }

    public void close() {
        synchronized (lock) {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }
}

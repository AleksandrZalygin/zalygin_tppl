import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DataCollector implements Runnable {
    private static final String SECRET_KEY = "isu_pt";
    private static final String GET_COMMAND = "get";
    private static final int SOCKET_TIMEOUT = 1000;
    private static final int RECONNECT_DELAY = 1000;
    private static final int REQUEST_INTERVAL = 3000;

    private final ServerConfig config;
    private final DataWriter writer;
    private final String serverName;
    private volatile boolean running = true;

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    private long validPackets = 0;
    private long invalidPackets = 0;

    public DataCollector(ServerConfig config, DataWriter writer, String serverName) {
        this.config = config;
        this.writer = writer;
        this.serverName = serverName;
    }

    @Override
    public void run() {
        while (running) {
            try {
                connect();
                collectData();
            } catch (Exception e) {
                System.err.println(serverName + ": Error - " + e.getMessage());
                closeConnection();
                if (running) {
                    sleep(RECONNECT_DELAY);
                }
            }
        }
        closeConnection();
    }

    private void connect() throws IOException {
        closeConnection();

        socket = new Socket(config.getHost(), config.getPort());
        socket.setSoTimeout(SOCKET_TIMEOUT);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        out.write(SECRET_KEY.getBytes(StandardCharsets.US_ASCII));
        out.flush();

        byte[] response = new byte[128];
        int bytesRead = 0;
        long startTime = System.currentTimeMillis();
        while (bytesRead < 7 && System.currentTimeMillis() - startTime < 2000) {
            if (in.available() > 0) {
                bytesRead += in.read(response, bytesRead, response.length - bytesRead);
            } else {
                sleep(10);
            }
        }

        System.out.println(serverName + ": Connected");
    }

    private void collectData() throws IOException {
        long lastRequestTime = 0;

        while (running) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastRequestTime >= REQUEST_INTERVAL) {
                sendGetCommand();
                lastRequestTime = currentTime;
            }

            try {
                readAndProcessData();
            } catch (SocketTimeoutException e) {
            }
        }
    }

    private void sendGetCommand() throws IOException {
        out.write(GET_COMMAND.getBytes(StandardCharsets.US_ASCII));
        out.flush();
    }

    private void readAndProcessData() throws IOException {
        byte[] data = new byte[config.getDataSize()];
        in.readFully(data);

        if (!validateChecksum(data)) {
            invalidPackets++;
            return;
        }

        if (!validateData(data)) {
            invalidPackets++;
            return;
        }

        validPackets++;
        String parsedData = parseData(data);
        writer.write(parsedData);
        System.out.println(parsedData);
    }

    private boolean validateData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.BIG_ENDIAN);

        long timestampMicros = buffer.getLong();
        long timestampSeconds = timestampMicros / 1000000;

        long currentSeconds = System.currentTimeMillis() / 1000;
        long oneYearSeconds = 365L * 24 * 60 * 60;

        if (timestampSeconds < currentSeconds - oneYearSeconds ||
            timestampSeconds > currentSeconds + oneYearSeconds) {
            return false;
        }

        if (config.getPort() == 5123) {
            float temperature = buffer.getFloat();
            short pressure = buffer.getShort();

            if (temperature < -50 || temperature > 100) {
                return false;
            }
            if (pressure < 900 || pressure > 1200) {
                return false;
            }
        } else {
            int x = buffer.getInt();
            int y = buffer.getInt();
            int z = buffer.getInt();

            if (Math.abs(x) > 1000 || Math.abs(y) > 1000 || Math.abs(z) > 1000) {
                return false;
            }
        }

        return true;
    }

    private boolean validateChecksum(byte[] data) {
        int sum = 0;
        for (int i = 0; i < data.length - 1; i++) {
            sum += (data[i] & 0xFF);
        }
        int expectedChecksum = sum % 256;
        int actualChecksum = data[data.length - 1] & 0xFF;
        return expectedChecksum == actualChecksum;
    }

    private String parseData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.BIG_ENDIAN);

        long timestampMicros = buffer.getLong();
        String timestamp = formatTimestamp(timestampMicros);

        if (config.getPort() == 5123) {
            float temperature = buffer.getFloat();
            short pressure = buffer.getShort();
            return String.format("%s | %s | Temperature: %.2f | Pressure: %d",
                    serverName, timestamp, temperature, pressure);
        } else {
            int x = buffer.getInt();
            int y = buffer.getInt();
            int z = buffer.getInt();
            return String.format("%s | %s | X: %d | Y: %d | Z: %d",
                    serverName, timestamp, x, y, z);
        }
    }

    private String formatTimestamp(long timestampMicros) {
        long timestampMillis = timestampMicros / 1000;
        Date date = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
        } catch (IOException ignored) {}
        try {
            if (in != null) in.close();
        } catch (IOException ignored) {}
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}

        out = null;
        in = null;
        socket = null;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }
}

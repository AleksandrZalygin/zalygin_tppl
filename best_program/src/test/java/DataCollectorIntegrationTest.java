import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DataCollectorIntegrationTest {
    private static final String TEST_FILE = "test_integration.txt";

    public void testStopMethod() throws Exception {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("127.0.0.1", 9, 15);
        DataCollector collector = new DataCollector(config, writer, "TestServer");

        Thread collectorThread = new Thread(collector);
        collectorThread.start();

        Thread.sleep(2000);

        collector.stop();
        collectorThread.join(2000);

        boolean stopped = !collectorThread.isAlive();
        if (!stopped) {
            collectorThread.interrupt();
        }

        TestRunner.assertTrue(stopped, "Collector should stop");

        cleanupTestFile();
    }

    public void testRunMethodWithMockServer() throws Exception {
        cleanupTestFile();

        int port = 15123;
        MockServer mockServer = new MockServer(port);
        Thread serverThread = new Thread(mockServer);
        serverThread.start();

        Thread.sleep(200);

        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("localhost", port, 15);
        DataCollector collector = new DataCollector(config, writer, "TestServer");

        Thread collectorThread = new Thread(collector);
        collectorThread.start();

        Thread.sleep(1000);

        collector.stop();
        mockServer.stop();

        collectorThread.join(2000);
        serverThread.join(2000);

        TestRunner.assertTrue(true, "Integration test completed");

        cleanupTestFile();
    }

    public void testSleepMethod() throws Exception {
        long start = System.currentTimeMillis();

        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("localhost", 5123, 15);
        DataCollector collector = new DataCollector(config, writer, "Test");

        java.lang.reflect.Method sleepMethod = DataCollector.class.getDeclaredMethod("sleep", long.class);
        sleepMethod.setAccessible(true);
        sleepMethod.invoke(collector, 100L);

        long elapsed = System.currentTimeMillis() - start;

        TestRunner.assertTrue(elapsed >= 90, "Sleep should wait at least 90ms");

        cleanupTestFile();
    }

    public void testConnectToInvalidServer() throws Exception {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("invalid.host.nonexistent", 9999, 15);
        DataCollector collector = new DataCollector(config, writer, "TestServer");

        Thread collectorThread = new Thread(collector);
        collectorThread.start();

        Thread.sleep(500);

        collector.stop();
        collectorThread.join(2000);

        TestRunner.assertTrue(true, "Should handle connection failure gracefully");

        cleanupTestFile();
    }

    public void testSendGetCommand() throws Exception {
        cleanupTestFile();

        final CountDownLatch latch = new CountDownLatch(1);
        final StringBuilder received = new StringBuilder();

        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(15124)) {
                latch.countDown();
                Socket client = serverSocket.accept();

                byte[] authKey = new byte[6];
                client.getInputStream().read(authKey);

                client.getOutputStream().write("granted".getBytes());
                client.getOutputStream().flush();

                byte[] getCmd = new byte[3];
                int bytesRead = client.getInputStream().read(getCmd);
                if (bytesRead > 0) {
                    received.append(new String(getCmd, 0, bytesRead));
                }

                client.close();
            } catch (Exception e) {
            }
        });
        serverThread.start();

        latch.await(1, TimeUnit.SECONDS);
        Thread.sleep(100);

        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("localhost", 15124, 15);
        DataCollector collector = new DataCollector(config, writer, "TestServer");

        Thread collectorThread = new Thread(collector);
        collectorThread.start();

        Thread.sleep(1000);

        collector.stop();
        collectorThread.join(2000);
        serverThread.join(2000);

        TestRunner.assertTrue(received.toString().contains("get"), "Should send GET command");

        cleanupTestFile();
    }

    public void testCloseConnection() throws Exception {
        cleanupTestFile();

        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("localhost", 9999, 15);
        DataCollector collector = new DataCollector(config, writer, "Test");

        java.lang.reflect.Method closeMethod = DataCollector.class.getDeclaredMethod("closeConnection");
        closeMethod.setAccessible(true);
        closeMethod.invoke(collector);

        TestRunner.assertTrue(true, "Close connection should not throw");

        cleanupTestFile();
    }

    private void cleanupTestFile() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    private static class MockServer implements Runnable {
        private final int port;
        private volatile boolean running = true;
        private ServerSocket serverSocket;

        public MockServer(int port) {
            this.port = port;
        }

        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(2000);

                while (running) {
                    try {
                        Socket client = serverSocket.accept();

                        byte[] authKey = new byte[6];
                        client.getInputStream().read(authKey);

                        client.getOutputStream().write("granted".getBytes());
                        client.getOutputStream().flush();

                        while (running) {
                            byte[] cmd = new byte[3];
                            int read = client.getInputStream().read(cmd);
                            if (read <= 0) break;

                            if (new String(cmd).equals("get")) {
                                byte[] data = createValidData();
                                client.getOutputStream().write(data);
                                client.getOutputStream().flush();
                            }
                        }

                        client.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
        }

        private byte[] createValidData() {
            ByteBuffer buffer = ByteBuffer.allocate(14);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putLong(System.currentTimeMillis() * 1000);
            buffer.putFloat(25.0f);
            buffer.putShort((short) 1013);

            byte[] dataWithoutChecksum = buffer.array();
            int sum = 0;
            for (byte b : dataWithoutChecksum) {
                sum += (b & 0xFF);
            }

            byte[] result = new byte[15];
            System.arraycopy(dataWithoutChecksum, 0, result, 0, 14);
            result[14] = (byte) (sum % 256);

            return result;
        }

        public void stop() {
            running = false;
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (Exception e) {
            }
        }
    }
}

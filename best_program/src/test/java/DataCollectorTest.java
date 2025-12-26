import java.io.File;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataCollectorTest {
    private static final String TEST_FILE = "test_collector.txt";

    public void testValidateChecksumValid() throws Exception {
        byte[] data = createDataWithChecksum(new byte[]{1, 2, 3, 4, 5});

        boolean result = invokeValidateChecksum(data);
        TestRunner.assertTrue(result, "Valid checksum should pass");

        cleanupTestFile();
    }

    public void testValidateChecksumInvalid() throws Exception {
        byte[] data = new byte[]{1, 2, 3, 4, 99};

        boolean result = invokeValidateChecksum(data);
        TestRunner.assertFalse(result, "Invalid checksum should fail");

        cleanupTestFile();
    }

    public void testValidateChecksumAllZeros() throws Exception {
        byte[] data = createDataWithChecksum(new byte[]{0, 0, 0, 0});

        boolean result = invokeValidateChecksum(data);
        TestRunner.assertTrue(result, "All zeros with valid checksum should pass");

        cleanupTestFile();
    }

    public void testValidateChecksumMaxValues() throws Exception {
        byte[] data = createDataWithChecksum(new byte[]{(byte)255, (byte)255, (byte)255});

        boolean result = invokeValidateChecksum(data);
        TestRunner.assertTrue(result, "Max values with valid checksum should pass");

        cleanupTestFile();
    }

    public void testValidateDataValidTimestamp() throws Exception {
        long currentTime = System.currentTimeMillis() * 1000;
        byte[] data = createServer1Data(currentTime, 25.0f, (short)1013);

        boolean result = invokeValidateData(data, 5123);
        TestRunner.assertTrue(result, "Valid timestamp should pass");

        cleanupTestFile();
    }

    public void testValidateDataFutureTimestamp() throws Exception {
        long futureTime = (System.currentTimeMillis() + 400L * 24 * 60 * 60 * 1000) * 1000;
        byte[] data = createServer1Data(futureTime, 25.0f, (short)1013);

        boolean result = invokeValidateData(data, 5123);
        TestRunner.assertFalse(result, "Future timestamp should fail");

        cleanupTestFile();
    }

    public void testValidateDataPastTimestamp() throws Exception {
        long pastTime = (System.currentTimeMillis() - 400L * 24 * 60 * 60 * 1000) * 1000;
        byte[] data = createServer1Data(pastTime, 25.0f, (short)1013);

        boolean result = invokeValidateData(data, 5123);
        TestRunner.assertFalse(result, "Past timestamp should fail");

        cleanupTestFile();
    }

    public void testValidateDataValidTemperature() throws Exception {
        long currentTime = System.currentTimeMillis() * 1000;

        byte[] data1 = createServer1Data(currentTime, -49.0f, (short)1000);
        TestRunner.assertTrue(invokeValidateData(data1, 5123), "Temperature -49 should pass");

        byte[] data2 = createServer1Data(currentTime, 99.0f, (short)1000);
        TestRunner.assertTrue(invokeValidateData(data2, 5123), "Temperature 99 should pass");

        byte[] data3 = createServer1Data(currentTime, 0.0f, (short)1000);
        TestRunner.assertTrue(invokeValidateData(data3, 5123), "Temperature 0 should pass");

        cleanupTestFile();
    }

    public void testValidateDataInvalidTemperature() throws Exception {
        long currentTime = System.currentTimeMillis() * 1000;

        byte[] data1 = createServer1Data(currentTime, -51.0f, (short)1000);
        TestRunner.assertFalse(invokeValidateData(data1, 5123), "Temperature -51 should fail");

        byte[] data2 = createServer1Data(currentTime, 101.0f, (short)1000);
        TestRunner.assertFalse(invokeValidateData(data2, 5123), "Temperature 101 should fail");

        cleanupTestFile();
    }

    public void testValidateDataValidPressure() throws Exception {
        long currentTime = System.currentTimeMillis() * 1000;

        byte[] data1 = createServer1Data(currentTime, 20.0f, (short)900);
        TestRunner.assertTrue(invokeValidateData(data1, 5123), "Pressure 900 should pass");

        byte[] data2 = createServer1Data(currentTime, 20.0f, (short)1200);
        TestRunner.assertTrue(invokeValidateData(data2, 5123), "Pressure 1200 should pass");

        cleanupTestFile();
    }

    public void testValidateDataInvalidPressure() throws Exception {
        long currentTime = System.currentTimeMillis() * 1000;

        byte[] data1 = createServer1Data(currentTime, 20.0f, (short)899);
        TestRunner.assertFalse(invokeValidateData(data1, 5123), "Pressure 899 should fail");

        byte[] data2 = createServer1Data(currentTime, 20.0f, (short)1201);
        TestRunner.assertFalse(invokeValidateData(data2, 5123), "Pressure 1201 should fail");

        cleanupTestFile();
    }

    public void testValidateDataValidCoordinates() throws Exception {
        long currentTime = System.currentTimeMillis() * 1000;

        byte[] data1 = createServer2Data(currentTime, 100, 200, 300);
        TestRunner.assertTrue(invokeValidateData(data1, 5124), "Valid coordinates should pass");

        byte[] data2 = createServer2Data(currentTime, -500, -600, -700);
        TestRunner.assertTrue(invokeValidateData(data2, 5124), "Negative coordinates should pass");

        byte[] data3 = createServer2Data(currentTime, 0, 0, 0);
        TestRunner.assertTrue(invokeValidateData(data3, 5124), "Zero coordinates should pass");

        cleanupTestFile();
    }

    public void testValidateDataInvalidCoordinates() throws Exception {
        long currentTime = System.currentTimeMillis() * 1000;

        byte[] data1 = createServer2Data(currentTime, 1001, 0, 0);
        TestRunner.assertFalse(invokeValidateData(data1, 5124), "X > 1000 should fail");

        byte[] data2 = createServer2Data(currentTime, 0, -1001, 0);
        TestRunner.assertFalse(invokeValidateData(data2, 5124), "Y < -1000 should fail");

        byte[] data3 = createServer2Data(currentTime, 0, 0, 2000);
        TestRunner.assertFalse(invokeValidateData(data3, 5124), "Z > 1000 should fail");

        cleanupTestFile();
    }

    public void testParseDataServer1() throws Exception {
        long timestamp = 1735200000000000L;
        byte[] data = createServer1Data(timestamp, 25.5f, (short)1013);

        String result = invokeParseData(data, 5123);
        TestRunner.assertTrue(result.contains("25.50") || result.contains("25,50"), "Should contain temperature");
        TestRunner.assertTrue(result.contains("1013"), "Should contain pressure");
        TestRunner.assertTrue(result.contains("Server1"), "Should contain server name");

        cleanupTestFile();
    }

    public void testParseDataServer2() throws Exception {
        long timestamp = 1735200000000000L;
        byte[] data = createServer2Data(timestamp, 10, 20, 30);

        String result = invokeParseData(data, 5124);
        TestRunner.assertTrue(result.contains("X: 10"), "Should contain X coordinate");
        TestRunner.assertTrue(result.contains("Y: 20"), "Should contain Y coordinate");
        TestRunner.assertTrue(result.contains("Z: 30"), "Should contain Z coordinate");
        TestRunner.assertTrue(result.contains("Server2"), "Should contain server name");

        cleanupTestFile();
    }

    public void testParseDataNegativeValues() throws Exception {
        long timestamp = 1735200000000000L;
        byte[] data = createServer2Data(timestamp, -5, -10, -15);

        String result = invokeParseData(data, 5124);
        TestRunner.assertTrue(result.contains("X: -5"), "Should contain negative X");
        TestRunner.assertTrue(result.contains("Y: -10"), "Should contain negative Y");
        TestRunner.assertTrue(result.contains("Z: -15"), "Should contain negative Z");

        cleanupTestFile();
    }

    private byte[] createDataWithChecksum(byte[] dataWithoutChecksum) {
        byte[] result = new byte[dataWithoutChecksum.length + 1];
        System.arraycopy(dataWithoutChecksum, 0, result, 0, dataWithoutChecksum.length);

        int sum = 0;
        for (byte b : dataWithoutChecksum) {
            sum += (b & 0xFF);
        }
        result[result.length - 1] = (byte)(sum % 256);

        return result;
    }

    private byte[] createServer1Data(long timestamp, float temperature, short pressure) {
        ByteBuffer buffer = ByteBuffer.allocate(14);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(timestamp);
        buffer.putFloat(temperature);
        buffer.putShort(pressure);

        return createDataWithChecksum(buffer.array());
    }

    private byte[] createServer2Data(long timestamp, int x, int y, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(timestamp);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.putInt(z);

        return createDataWithChecksum(buffer.array());
    }

    private boolean invokeValidateChecksum(byte[] data) throws Exception {
        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("localhost", 5123, data.length);
        DataCollector collector = new DataCollector(config, writer, "Test");

        Method method = DataCollector.class.getDeclaredMethod("validateChecksum", byte[].class);
        method.setAccessible(true);

        return (boolean) method.invoke(collector, data);
    }

    private boolean invokeValidateData(byte[] data, int port) throws Exception {
        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("localhost", port, data.length);
        DataCollector collector = new DataCollector(config, writer, "Test");

        Method method = DataCollector.class.getDeclaredMethod("validateData", byte[].class);
        method.setAccessible(true);

        return (boolean) method.invoke(collector, data);
    }

    private String invokeParseData(byte[] data, int port) throws Exception {
        DataWriter writer = new DataWriter(TEST_FILE);
        ServerConfig config = new ServerConfig("localhost", port, data.length);
        String serverName = port == 5123 ? "Server1" : "Server2";
        DataCollector collector = new DataCollector(config, writer, serverName);

        Method method = DataCollector.class.getDeclaredMethod("parseData", byte[].class);
        method.setAccessible(true);

        return (String) method.invoke(collector, data);
    }

    private void cleanupTestFile() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}

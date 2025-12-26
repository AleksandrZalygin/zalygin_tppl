public class ServerConfigTest {

    public void testConstructorAndGetters() {
        ServerConfig config = new ServerConfig("localhost", 8080, 15);

        TestRunner.assertEquals("localhost", config.getHost(), "Host should match");
        TestRunner.assertEquals(8080, config.getPort(), "Port should match");
        TestRunner.assertEquals(15, config.getDataSize(), "DataSize should match");
    }

    public void testDifferentValues() {
        ServerConfig config1 = new ServerConfig("192.168.1.1", 5123, 21);
        TestRunner.assertEquals("192.168.1.1", config1.getHost(), "Host should be 192.168.1.1");
        TestRunner.assertEquals(5123, config1.getPort(), "Port should be 5123");
        TestRunner.assertEquals(21, config1.getDataSize(), "DataSize should be 21");

        ServerConfig config2 = new ServerConfig("example.com", 9999, 100);
        TestRunner.assertEquals("example.com", config2.getHost(), "Host should be example.com");
        TestRunner.assertEquals(9999, config2.getPort(), "Port should be 9999");
        TestRunner.assertEquals(100, config2.getDataSize(), "DataSize should be 100");
    }

    public void testMultipleInstances() {
        ServerConfig config1 = new ServerConfig("host1", 1111, 10);
        ServerConfig config2 = new ServerConfig("host2", 2222, 20);

        TestRunner.assertEquals("host1", config1.getHost(), "Config1 host should not change");
        TestRunner.assertEquals("host2", config2.getHost(), "Config2 host should be independent");
    }
}

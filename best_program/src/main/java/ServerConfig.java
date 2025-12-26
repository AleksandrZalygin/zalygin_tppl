public class ServerConfig {
    private final String host;
    private final int port;
    private final int dataSize;

    public ServerConfig(String host, int port, int dataSize) {
        this.host = host;
        this.port = port;
        this.dataSize = dataSize;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getDataSize() {
        return dataSize;
    }
}

package me.theminecoder.redissonminecraft.config.server;

/**
 * @author theminecoder
 * @version 1.0
 */
@SuppressWarnings("unused")
public class ServerAddress {
    private String hostname;
    private int port;

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }
}

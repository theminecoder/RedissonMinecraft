package me.theminecoder.redissonminecraft.config;

/**
 * @author theminecoder
 * @version 1.0
 */
@SuppressWarnings("unused")
public class RedissonMinecraftBackendConfig extends RedissonMinecraftConfig {

    private static String forcedServerName = null;

    public static String getForcedServerName() {
        return forcedServerName;
    }

    public static void setForcedServerName(String forcedServerName) {
        RedissonMinecraftBackendConfig.forcedServerName = forcedServerName;
    }

    @SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
    private String serverName = "lobby";

    public String getServerName() {
        return forcedServerName == null || forcedServerName.trim().length() <= 0 ? serverName : forcedServerName;
    }

}

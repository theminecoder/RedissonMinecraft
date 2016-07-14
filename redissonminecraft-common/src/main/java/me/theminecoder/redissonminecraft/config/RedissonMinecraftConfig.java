package me.theminecoder.redissonminecraft.config;

import java.util.Map;

/**
 * @author theminecoder
 * @version 1.0
 */
public class RedissonMinecraftConfig {

    @SuppressWarnings("unused")
    private Servers servers;

    @SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
    private boolean enableDynamicServers = false;

    public Servers getServers() {
        return servers;
    }

    public boolean isEnableDynamicServers() {
        return enableDynamicServers;
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static class Servers {
        private String mode;
        private Map<String, Object> config;

        public String getMode() {
            return mode;
        }

        public Map<String, Object> getConfig() {
            return config;
        }

    }

}

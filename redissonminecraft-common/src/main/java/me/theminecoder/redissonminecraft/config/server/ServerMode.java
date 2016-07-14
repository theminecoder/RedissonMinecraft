package me.theminecoder.redissonminecraft.config.server;

import java.util.Arrays;
import java.util.List;

/**
 * @author theminecoder
 * @version 1.0
 */
public enum ServerMode {

    SINGLE_SERVER("single"),
    MASTER_SLAVE("masterslave"),
    CLUSTER("multi", "multiple"),
    SENTINAL,
    ELASTICACHE("aws_elasticache");

    private final List<String> aliases;

    ServerMode(String... aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    public static ServerMode getByName(String name) {
        for (ServerMode mode : values()) {
            if (mode.name().equalsIgnoreCase(name) || mode.aliases.stream().filter(alias -> alias.equalsIgnoreCase(name)).findFirst().isPresent()) {
                return mode;
            }
        }
        return null;
    }

}

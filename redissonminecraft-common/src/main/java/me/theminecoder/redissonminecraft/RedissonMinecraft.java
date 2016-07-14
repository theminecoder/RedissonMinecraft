package me.theminecoder.redissonminecraft;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import me.theminecoder.redissonminecraft.config.RedissonMinecraftConfig;
import me.theminecoder.redissonminecraft.config.server.ServerMode;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.api.RedissonReactiveClient;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author theminecoder
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public final class RedissonMinecraft {

    private static RedissonClient client;
    private static RedissonReactiveClient reactiveClient;

    @SuppressWarnings("unchecked")
    public static <C extends RedissonMinecraftConfig> C init(File configFile, Class<C> configType, Logger logger) {
        logger.info("Loading Config....");
        C pluginConfig;
        try {
            if (!configFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                configFile.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
                try (InputStream in = RedissonMinecraft.class.getResourceAsStream("/config.yml");
                     OutputStream out = new FileOutputStream(configFile)) {
                    ByteStreams.copy(in, out);
                }
            }
            YamlConfig yamlConfig = new YamlConfig();
            yamlConfig.setPrivateFields(true);
            pluginConfig = new YamlReader(new FileReader(configFile), yamlConfig).read(configType);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config!", e);
        }

        if (client != null) {
            return pluginConfig;
        }

        //Do the actual init here...
        Config config = new Config();
        ServerMode mode = ServerMode.getByName(pluginConfig.getServers().getMode());
        if(mode==null) {
            throw new IllegalArgumentException("No mode defined with the name \""+pluginConfig.getServers().getMode()+"\"!");
        }
        logger.info("Configuring to use "+mode.name()+" connection mode....");
        switch (mode) {
            case SINGLE_SERVER: {
                config.useSingleServer().setAddress(pluginConfig.getServers().getConfig().getOrDefault("server", "127.0.0.1:6379").toString());
                config.useSingleServer().setPassword(stringOrNull(pluginConfig.getServers().getConfig().get("password").toString()));
            }
            break;
            case MASTER_SLAVE: {
                config.useMasterSlaveServers().setMasterAddress(pluginConfig.getServers().getConfig().getOrDefault("master", "127.0.0.1:6379").toString());
                config.useMasterSlaveServers().addSlaveAddress(((List<String>) pluginConfig.getServers().getConfig().getOrDefault("slaves", ImmutableList.of())).stream().toArray(String[]::new));
                config.useMasterSlaveServers().setPassword(stringOrNull(pluginConfig.getServers().getConfig().get("password").toString()));
            }
            break;
            case CLUSTER: {
                config.useClusterServers().addNodeAddress(((List<String>) pluginConfig.getServers().getConfig().getOrDefault("servers", ImmutableList.of("127.0.0.1:6379"))).stream().toArray(String[]::new));
                config.useClusterServers().setScanInterval(Integer.valueOf(pluginConfig.getServers().getConfig().getOrDefault("scanInterval", 1000).toString()));
                config.useClusterServers().setPassword(stringOrNull(pluginConfig.getServers().getConfig().get("password").toString()));
            }
            break;
            case SENTINAL: {
                config.useSentinelServers().setMasterName(pluginConfig.getServers().getConfig().getOrDefault("masterName", "master").toString());
                config.useSentinelServers().addSentinelAddress(((List<String>) pluginConfig.getServers().getConfig().getOrDefault("servers", ImmutableList.of("127.0.0.1:6379"))).stream().toArray(String[]::new));
                config.useSentinelServers().setPassword(stringOrNull(pluginConfig.getServers().getConfig().get("password").toString()));
            }
            break;
            case ELASTICACHE: {
                config.useElasticacheServers().addNodeAddress(((List<String>) pluginConfig.getServers().getConfig().getOrDefault("servers", ImmutableList.of("127.0.0.1:6379"))).stream().toArray(String[]::new));
                config.useElasticacheServers().setScanInterval(Integer.valueOf(pluginConfig.getServers().getConfig().getOrDefault("scanInterval", 1000).toString()));
                config.useElasticacheServers().setPassword(stringOrNull(pluginConfig.getServers().getConfig().get("password").toString()));
            }
            break;
        }

        logger.info("Creating clients....");
        client = Redisson.create(config);
        reactiveClient = Redisson.createReactive(config);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Shutting down clients....");
                client.shutdown();
                reactiveClient.shutdown();
            }
        });

        logger.info("And now it's back to you, plugin!");
        return pluginConfig;
    }

    public static RedissonClient getClient() {
        return client;
    }

    @SuppressWarnings("unused")
    public static RedissonReactiveClient getReactiveClient() {
        return reactiveClient;
    }

    private static String stringOrNull(String string) {
        return string == null || string.trim().length() <= 0 ? null : string;
    }
}

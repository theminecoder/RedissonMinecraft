package me.theminecoder.redissonminecraft;

import com.google.inject.Inject;
import me.theminecoder.redissonminecraft.config.RedissonMinecraftBackendConfig;
import org.redisson.core.RMapCache;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "redissonminecraft",
        name = "RedissonMinecraft",
        version = "1.0",
        authors = {
                "theminecoder"
        }
)
public class RedissonMinecraftSponge {

    @Inject
    @SuppressWarnings("unused")
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    @SuppressWarnings("unused")
    private Path configFile;

    private RedissonMinecraftBackendConfig config;

    private RMapCache<String, String> backendServerMap;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        this.config = RedissonMinecraft.init(configFile.toFile(), RedissonMinecraftBackendConfig.class, java.util.logging.Logger.getLogger(logger.getName()));
        if (this.config.isEnableDynamicServers()) {
            this.backendServerMap = RedissonMinecraft.getClient().getMapCache("redissonminecraft__backend-servers");
            Sponge.getScheduler().createTaskBuilder().async().intervalTicks(200).execute(() -> {
                InetSocketAddress address = Sponge.getServer().getBoundAddress().orElseThrow(() -> new IllegalStateException("Bound Address not available, server not yet bound?"));
                backendServerMap.put(config.getServerName(), address.getHostName() + ":" + address.getPort(), 1, TimeUnit.MINUTES);
            }).name("Redisson Minecraft - Dynamic Server Config Sender").submit(this);
        }
    }

}
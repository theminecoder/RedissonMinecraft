package me.theminecoder.redissonminecraft;

import me.theminecoder.redissonminecraft.config.RedissonMinecraftBackendConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.core.RMapCache;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class RedissonMinecraftSpigot extends JavaPlugin implements Listener {

    private static RedissonMinecraftBackendConfig config;

    private RMapCache<String, String> backendServerMap;

    @Override
    public void onEnable() {
        this.config = RedissonMinecraft.init(new File(this.getDataFolder(), "config.yml"), RedissonMinecraftBackendConfig.class, this.getLogger());
        if (this.config.isEnableDynamicServers()) {
            this.backendServerMap = RedissonMinecraft.getClient().getMapCache("redissonminecraft__backend-servers");
            this.getServer().getScheduler().runTaskTimerAsynchronously(this, () ->
                    backendServerMap.put(config.getServerName(), Bukkit.getIp() + ":" + Bukkit.getPort(), 10, TimeUnit.SECONDS)
                    , 0, 60);
        }
    }

    public static RedissonMinecraftBackendConfig getPluginConfig() {
        return config;
    }
}

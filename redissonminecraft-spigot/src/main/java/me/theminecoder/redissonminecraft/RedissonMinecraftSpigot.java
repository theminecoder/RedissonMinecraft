package me.theminecoder.redissonminecraft;

import me.theminecoder.redissonminecraft.config.RedissonMinecraftBackendConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class RedissonMinecraftSpigot extends JavaPlugin implements Listener {

    private RedissonMinecraftBackendConfig config;

    @Override
    public void onEnable() {
        this.config = RedissonMinecraft.init(new File(this.getDataFolder(), "config.yml"), RedissonMinecraftBackendConfig.class, this.getLogger());

        if (this.config.isEnableDynamicServers()) {
            this.getServer().getScheduler().runTaskTimerAsynchronously(this, () ->
                    RedissonMinecraft.getClient().getMapCache("redissonminecraft__backend-servers").put(config.getServerName(), Bukkit.getIp() + ":" + Bukkit.getPort(), 1, TimeUnit.MINUTES)
                    , 0, 200);
        }
    }
}

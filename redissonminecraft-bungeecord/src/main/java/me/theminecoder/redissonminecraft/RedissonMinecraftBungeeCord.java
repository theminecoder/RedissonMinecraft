package me.theminecoder.redissonminecraft;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import org.redisson.core.RMap;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class RedissonMinecraftBungeeCord extends Plugin {

    private RedissonMinecraftBungeeCordConfig config;

    private RMap<String, String> backendServerMap;

    @Override
    public void onEnable() {
        this.config = RedissonMinecraft.init(new File(this.getDataFolder(), "config.yml"), RedissonMinecraftBungeeCordConfig.class, this.getLogger());

        if (this.config.isEnableDynamicServers()) {
            this.backendServerMap = RedissonMinecraft.getClient().getMapCache("redissonminecraft__backend-servers");
            this.getProxy().getScheduler().schedule(this, () -> {
                backendServerMap.readAllKeySetAsync().addListener(keyFuture -> {
                    if (keyFuture.isSuccess()) {
                        backendServerMap.getAllAsync((Set<String>) keyFuture.getNow()).addListener(future -> {
                            if (future.isSuccess()) {
                                Map<String, String> backendServers = (Map<String, String>) future.getNow();
                                Map<String, ServerInfo> currentServers = this.getProxy().getServers();

                                for (Iterator<Map.Entry<String, ServerInfo>> currentServerIterator = currentServers.entrySet().iterator(); currentServerIterator.hasNext(); ) {
                                    Map.Entry<String, ServerInfo> currentServerEntry = currentServerIterator.next();
                                    if (backendServers.containsKey(currentServerEntry.getKey())) {
                                        if (!backendServers.get(currentServerEntry.getKey()).equalsIgnoreCase(
                                                currentServerEntry.getValue().getAddress().getHostName() + ":" + currentServerEntry.getValue().getAddress().getPort()
                                        )) {
                                            //noinspection deprecation
                                            currentServerEntry.getValue().getPlayers().forEach(player ->
                                                    player.disconnect("That server has changed IP while online, duplicate server?"));
                                            String[] newIp = backendServers.get(currentServerEntry.getKey()).split(":");
                                            this.getLogger().warning("Server IP for \"" + currentServerEntry.getKey() + "\" updated!");
                                            currentServerEntry.setValue(this.getProxy().constructServerInfo(currentServerEntry.getKey(),
                                                    new InetSocketAddress(newIp[0], Integer.valueOf(newIp[1])), "", false));
                                        }
                                    } else {
                                        if(this.config.getIgnoredDynamicServers().stream().filter(server -> server.equalsIgnoreCase(currentServerEntry.getKey())).findFirst().isPresent()) {
                                            continue;
                                        }
                                        //noinspection deprecation
                                        currentServerEntry.getValue().getPlayers().forEach(player ->
                                                player.disconnect("That server has timed out, broken connection?"));
                                        this.getLogger().warning("Server IP for \"" + currentServerEntry.getKey() + "\" removed!");
                                        currentServerIterator.remove();
                                    }
                                }

                                backendServers.entrySet().stream().filter(serverEntry -> !currentServers.containsKey(serverEntry.getKey())).forEach(serverEntry -> {
                                    String[] newIp = backendServers.get(serverEntry.getKey()).split(":");
                                    currentServers.put(serverEntry.getKey(), this.getProxy().constructServerInfo(serverEntry.getKey(),
                                            new InetSocketAddress(newIp[0], Integer.valueOf(newIp[1])), "", false));
                                    this.getLogger().info("Server IP for \"" + serverEntry.getKey() + "\" added!");
                                });
                            }
                        });
                    }
                });
            }, 0, 3, TimeUnit.SECONDS);
        }
    }
}

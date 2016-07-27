package me.theminecoder.redissonminecraft.events;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Event;

/**
 * @author theminecoder
 * @version 1.0
 */
public abstract class DynamicServerEvent extends Event {

    private ServerInfo serverInfo;

    public DynamicServerEvent(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }
}

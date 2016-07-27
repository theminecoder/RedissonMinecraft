package me.theminecoder.redissonminecraft.events;

import net.md_5.bungee.api.config.ServerInfo;

/**
 * @author theminecoder
 * @version 1.0
 */
public class DynamicServerUpdatedEvent extends DynamicServerEvent {

    private ServerInfo newServerInfo;

    public DynamicServerUpdatedEvent(ServerInfo serverInfo, ServerInfo newServerInfo) {
        super(serverInfo);
        this.newServerInfo = newServerInfo;
    }

    public ServerInfo getOldServerInfo() {
        return this.getServerInfo();
    }

    public ServerInfo getNewServerInfo() {
        return newServerInfo;
    }
}

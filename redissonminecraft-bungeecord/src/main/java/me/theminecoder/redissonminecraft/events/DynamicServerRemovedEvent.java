package me.theminecoder.redissonminecraft.events;

import net.md_5.bungee.api.config.ServerInfo;

/**
 * @author theminecoder
 * @version 1.0
 */
public class DynamicServerRemovedEvent extends DynamicServerEvent {

    public DynamicServerRemovedEvent(ServerInfo serverInfo) {
        super(serverInfo);
    }

}

package me.theminecoder.redissonminecraft;

import com.google.common.collect.Lists;
import me.theminecoder.redissonminecraft.config.RedissonMinecraftConfig;

import java.util.List;

/**
 * @author theminecoder
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class RedissonMinecraftBungeeCordConfig extends RedissonMinecraftConfig {

    @SuppressWarnings({"CanBeFinal"})
    private List<String> ignoredDynamicServers = Lists.newArrayList();

    public List<String> getIgnoredDynamicServers() {
        return ignoredDynamicServers;
    }

}

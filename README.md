# RedissonMinecraft
This integrates [Redisson] into most major minecraft server platforms.

It also serves as a stand alone plugin for bungeecord to have dynamic server registration.

[Download it!](https://github.com/theminecoder/RedissonMinecraft/releases)

### What is [Redisson]?
[Redisson] is a Java data structure wrapper on top of a redis client to provide a distributed data object store. 
It provides distributed maps, queues, sets, lists, locks and lots more.  

### Usage
1. Require the plugin in your `plugin.yml`'s `depend` section.
2. Get the client from `RedissonMinecraft.getClient()` (Sync and Async Client) or `RedissonMinecraft.getReactiveClient()`
    (Reactive Client) in your plugin.
3. Retrieve the wrapper objects by calling `client.<Type...>get%Object%(%key%)` replacing `<Type...>` with the types you wish to place in that object,
    `%Object%` with the wrapper object type you wish to use and `%key%` with the redis key in which to store the data. **You must use the same wrapper type for the same key!** 

### Server Installation
1. Place the plugin into your plugin folder.
2. Start the server once and then stop it.
3. Open up the generated config file and insert your redis settings, for most setups you will either be using the single mode or the cluster mode.
4. Start the server again.

#### Optional Dynamic Server Setup
1. Once you have connected the plugin to your redis server(s), open up the config again and uncomment `enableDynamicServers: true`
2. On your spigot server you will need to set the `serverName` option to be different in each config and make sure that your `server.properties` has configured `server-ip` and `server-port` fields.
3. On bungeecord you can uncomment `ignoredDynamicServers` to specify a list of server to not remove/disconnect players from if the heartbeat ping runs out in redis. This is great for fallback servers within the network.

Also of note are events included with the bungeecord plugin. `DynamicServerAddedEvent`, `DynamicServerRemovedEvent` and `DynamicServerUpdatedEvent` are all called after the operation is completed by the dynamic server updater for other plugins to hook into.

### Include in your project
Where `%servertype%` is one of `bungeecord`,`spigot` or `sponge`.

Using Gradle:
```gradle
repositories { 
    maven { 
        url "http://dl.bintray.com/theminecoder/maven" 
    }
}

dependencies {
    compile 'me.theminecoder:redissonminecraft-%servertype%:1.0'
}
```

Using Maven:
```xml
<repositories>
    <repository>
        <id>theminecoder-bintray</id>
        <url>http://dl.bintray.com/theminecoder/maven</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>me.theminecoder</groupId>
        <artifactId>redissonminecraft-%servertype%</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>    
```

[Redisson]: https://github.com/mrniko/redisson
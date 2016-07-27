# RedissonMinecraft
This integrates [Redisson](https://github.com/mrniko/redisson) into most major minecraft server platforms.

It also serves as a stand alone plugin for bungeecord to have dynamic server registration.

### Include in your project
Where `servertype` is one of `bungeecord`,`spigot` or `sponge`.

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
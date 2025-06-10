# DVZConfig
Configuration library for bukkit plugins based on [Configurate](https://github.com/spongepowered/configurate)

## Installation
At the moment, there is **4 modules**
1. **core** - contains the main logic of the library
2. **lang-yml** - contains the implementation for YML format
3. **platform-spigot** - contains additional serializers for the Spigot platform
4. **platform-paper** - contains additional serializers for Paper. Includes **Platform-Spigot**

### Maven

Add the following repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>my-maven-repo</id>
        <url>https://mymavenrepo.com/repo/AzWPavD3lYKKBaGswiVO/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>me.thedivazo.libs.DVZConfig</groupId>
        <artifactId>[module-name]</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

### Gradle (Kotlin DSL)

Add the following to your `build.gradle.kts`:

```kotlin
repositories {
    maven("https://mymavenrepo.com/repo/AzWPavD3lYKKBaGswiVO/")
}

dependencies {
    implementation("me.thedivazo.libs.DVZConfig:[module-name]:1.0.1")
}
```

### Gradle (Groovy DSL)

Add the following to your `build.gradle`:

```groovy
repositories {
    maven {
        url("https://mymavenrepo.com/repo/AzWPavD3lYKKBaGswiVO/")
    }
}

dependencies {
    implementation 'me.thedivazo.libs.DVZConfig:[module-name]:1.0.1'
}
```

## Guide to use

First, you should know what **ConfigManager** is

**ConfigManager** - the point for working with the configuration. <br>
You can define your **own Config manager extended ConfigManager interface** or use the **ConfigManagerImpl**


Full example for uses a **paper platform** and **lang-yml** modules:
```java
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
class MainConfig {
    String param1 = "defaultValue"; // default value required!!
    String param2 = "defaultValue";
}

public class YouPlugin extends JavaPlugin {
    private final ConfigManager configManager;

    @Override
    public void onEnable() {
        loadConfigManager();
        MainConfig mainConfig = configManager.getConfig(MainConfig.class);
        getLogger().info(mainConfig.param1);
    }

    public void reload() {
        configManager.load();
    }
    
    public void loadConfigManager() {
        TypeSerializerCollection typeSerializerCollection = TypeSerializerCollection.builder()
                .reguster(Location.class, LocationScalarSerializer.DEFAULT) // from platform-paper or platform-spigot module
                .register(Color.class, BukkitColorSerializer.DEFAULT) // from platform-paper or platform-spigot module
                .register(PotionEffectType.class, PotionEffectTypeSerializer.DEFAULT) // from platform-paper module
                .build();

        ConfigLoader configLoader = YamlConfigLoader.getConfigLoader(typeSerializerCollection); // from lang-eml module

        ConfigContainer configContainer = ConfigContainer.builder()
                .loader(configLoader)
                .addConfig(getDataFolder().toPath().resolve("config.yml"), MainConfig.class)
                .build();
        ConfigManager configManager = new ConfigManagerImpl(configContainer);

        this.configManager = configManager;
    }
}
```

This lib provides several of its classes for serialization. One of them is [HierarchyClassSerializer](/core/src/main/java/me/thedivazo/libs/dvzconfig/core/serializer/HierarchyClassSerializer.java). To understand it, it's better to read its javadoc

For more information see javadoc and https://github.com/SpongePowered/Configurate and more examples in **test module**

## Compiling
Compilation requires JDK 21 and up.<br>
To compile the plugin, run ./gradlew build from the terminal.<br>
Once the plugin compiles, grab the jar from `/target` folder.<br>
The universal jar contains all modules for all supported platforms.<br>



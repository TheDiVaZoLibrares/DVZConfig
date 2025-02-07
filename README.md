# DVZConfig
Configuration library for bukkit plugins based on spongepowered configuring

Bukkit servers' configuration library (Spigot and Paper inclusive) based on [Configurate](https://github.com/spongepowered/configurate)

Wiki will appear in this library in the near future.

you can find examples of use of plugin in **tests**

## Installation

На данный момент существует **4 модуля**
1. **Core** - содержится основная логика библиотеки
2. **Lang-yml** - содержит реализацию для формата yml
3. **Platform-spigot** - содержит дополнительные сериализаторы для spigot платформы
4. **Platform-paper** - содержит дополнительные сериализаторы для Paper. Включает в себя **Platform-spigot**

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
        <version>1.0.0-SNAPSHOT</version>
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
    implementation("me.thedivazo.libs.DVZConfig:[module-name]:1.0.0-SNAPSHOT")
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
    implementation 'me.thedivazo.libs.DVZConfig:[module-name]:1.0.0-SNAPSHOT'
}
```


## Compiling
Compilation requires JDK 21 and up.
To compile the plugin, run ./gradlew build from the terminal.
Once the plugin compiles, grab the jar from `/target` folder.
The universal jar contains all modules for all supported platforms.

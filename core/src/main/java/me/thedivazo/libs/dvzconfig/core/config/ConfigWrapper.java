package me.thedivazo.libs.dvzconfig.core.config;

import me.thedivazo.libs.dvzconfig.core.util.ReflectionUtil;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public final class ConfigWrapper<T> {
    private ConfigLoader<T> configLoader;
    private Path pathToFile;
    private T actualConfig;
    private final T defaultConfig;
    private Class<? extends T> configClass;

    public ConfigWrapper(@Nullable Path pathToFile,@Nullable ConfigLoader<T> configLoader, T defaultConfig) {
        if (!ReflectionUtil.hasAnnotation(defaultConfig.getClass(), ConfigSerializable.class)) {
            throw new IllegalArgumentException("Config class has not have @ConfigSerializable annotation");
        }
        else if (!ReflectionUtil.hasEmptyConstructor(defaultConfig.getClass())) {
            throw new IllegalArgumentException("Config class has not have empty constructor");
        }
        this.pathToFile = pathToFile;
        this.configLoader = configLoader;
        this.configClass = (Class<? extends T>) defaultConfig.getClass();
        this.defaultConfig = defaultConfig;
    }

    public void updatePath(Path newPath) {
        this.pathToFile = newPath;
    }

    public void load() {
        actualConfig = configLoader.load(pathToFile, defaultConfig);
    }

    public void save() {
        configLoader.save(pathToFile, actualConfig);
    }

    public T getConfig() {
        return actualConfig;
    }

    public Class<? extends T> getConfigClass() {
        return configClass;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ConfigWrapper<?> that = (ConfigWrapper<?>) o;
        return pathToFile.equals(that.pathToFile) && configClass.equals(that.configClass);
    }

    @Override
    public int hashCode() {
        int result = pathToFile.hashCode();
        result = 31 * result + configClass.hashCode();
        return result;
    }


}

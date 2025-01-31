package me.thedivazo.libs.dvzconfig.core;

import me.thedivazo.libs.dvzconfig.core.util.ReflectionUtil;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public class ConfigWrapper<T> {
    protected ConfigLoader<T> configLoader;
    protected Path pathToFile;
    protected T actualConfig;
    protected Class<? extends T> configClass;

    public ConfigWrapper(Path pathToFile, ConfigLoader<T> configLoader, Class<? extends T> configClass) {
        if (!ReflectionUtil.hasAnnotation(configClass, ConfigSerializable.class)) {
            throw new IllegalArgumentException("Config class has not have @ConfigSerializable annotation");
        }
        else if (!ReflectionUtil.hasEmptyConstructor(configClass)) {
            throw new IllegalArgumentException("Config class has not have empty constructor");
        }
        this.pathToFile = pathToFile;
        this.configLoader = configLoader;
        this.configClass = configClass;
    }

    public void load(boolean save) {
        actualConfig = configLoader.load(pathToFile, configClass, save);
    }

    public void loadOrCreate() {
        boolean isExist = pathToFile.toFile().exists();
        load(!isExist);
    }

    public void loadAndSave() {
        load(true);
    }

    public void save() {
        configLoader.save(pathToFile, actualConfig);
    }

    public T getConfig() {
        return actualConfig;
    }
}

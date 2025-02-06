package me.thedivazo.libs.dvzconfig.core.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public abstract class ConfigLoader<T> {
    protected final TypeSerializerCollection[] serializerCollections;

    protected ConfigLoader(TypeSerializerCollection[] serializerCollections) {
        this.serializerCollections = Arrays.copyOf(serializerCollections, serializerCollections.length);
    }

    public T load(Path pathToFile, T defaultValue) {
        if (defaultValue == null) {
            throw new IllegalArgumentException("default config is null");
        }
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = getLoader(pathToFile);
            CommentedConfigurationNode node = loader.load();
            if (node.isNull()) {
                node.set(defaultValue.getClass(), defaultValue);
                loader.save(node);
            }
            T config = (T) node.get(defaultValue.getClass());
            return config;
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Path pathToFile, T config) {
        if (config == null) {
            throw new IllegalArgumentException("default config is null");
        }
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = getLoader(pathToFile);
            CommentedConfigurationNode node = loader.load();
            node.set(config.getClass(), config);
            loader.save(node);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract ConfigurationLoader<CommentedConfigurationNode> getLoader(Path pathToFile);
}

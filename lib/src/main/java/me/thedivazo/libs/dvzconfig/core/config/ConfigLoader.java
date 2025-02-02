package me.thedivazo.libs.dvzconfig.core.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.nio.file.Path;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public abstract class ConfigLoader<T> {
    protected final TypeSerializerCollection[] serializerCollections;

    protected ConfigLoader(TypeSerializerCollection[] serializerCollections) {
        this.serializerCollections = serializerCollections;
    }

    public T load(Path pathToFile, Class<? extends T> clazz, boolean save) {
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = getLoader(pathToFile);
            CommentedConfigurationNode node = loader.load();
            T config = node.get(clazz);
            if (save) {
                node.set(clazz, config);
                loader.save(node);
            }
            return config;
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Path pathToFile, T config) {
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = getLoader(pathToFile);
            CommentedConfigurationNode node = loader.load();
            node.set(config.getClass(), config);
            loader.save(node);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract ConfigurationLoader<CommentedConfigurationNode> getLoader(Path pathToFile);
}

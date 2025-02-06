package me.thedivazo.libs.dvzconfig.yaml;

import me.thedivazo.libs.dvzconfig.core.config.ConfigLoader;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public class YamlConfigLoader<T> extends ConfigLoader<T> {
    private static final YamlConfigLoader<?> DEFAULT = new YamlConfigLoader<>(NodeStyle.BLOCK, 4);

    private final int indent;
    private final NodeStyle nodeStyle;

    @SuppressWarnings("unchecked")
    public static <T> YamlConfigLoader<T> getConfigLoader() {
        return (YamlConfigLoader<T>) DEFAULT;
    }

    public static <T> YamlConfigLoader<T> getConfigLoader(TypeSerializerCollection... serializerCollections) {
        return new YamlConfigLoader<>(NodeStyle.BLOCK, 4, serializerCollections);
    }

    public YamlConfigLoader(NodeStyle nodeStyle, int indent, TypeSerializerCollection... serializerCollections) {
        super(serializerCollections);
        this.indent = indent;
        this.nodeStyle = nodeStyle;
    }

    @Override
    public ConfigurationLoader<CommentedConfigurationNode> getLoader(Path pathToFile) {
        return YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .serializers(builder -> {
                            for (TypeSerializerCollection serializerCollection : serializerCollections) {
                                builder.registerAll(serializerCollection);
                            }
                        })
                )
                .indent(indent)
                .nodeStyle(nodeStyle)
                .path(pathToFile)
                .build();
    }
}

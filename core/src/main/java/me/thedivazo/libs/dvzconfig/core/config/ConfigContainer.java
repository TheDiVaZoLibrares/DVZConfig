package me.thedivazo.libs.dvzconfig.core.config;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TheDiVaZo
 * created on 02.02.2025
 */
public class ConfigContainer {
    private final Map<Class<?>, ConfigWrapper<?>> container;

    public ConfigContainer(Set<ConfigWrapper<?>> configWrappers) {
        this.container = configWrappers.stream().collect(Collectors.toMap(ConfigWrapper::getConfigClass, configWrapper -> configWrapper));
    }

    public void loadAll() {
        for (ConfigWrapper<?> value : container.values()) {
            value.load();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> ConfigWrapper<T> getConfig(Class<T> clazz) {
        Object config = container.get(clazz);
        if (config == null) {
            throw new IllegalArgumentException("No config for class: " + clazz);
        }
        return (ConfigWrapper<T>) config;
    }

    public Map<Class<?>, ConfigWrapper<?>> getContainer() {
        return container;
    }
}

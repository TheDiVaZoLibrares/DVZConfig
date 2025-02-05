package me.thedivazo.libs.dvzconfig.core.config;

import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 02.02.2025
 */
public class ConfigContainer {
    private final Map<Class<?>, ConfigWrapper<?>> container;

    private ConfigContainer(ConfigWrapperClassMapBuilder container) {
        this.container = Map.copyOf(container.builder());
    }

    public void loadAll() {
        for (ConfigWrapper<?> value : container.values()) {
            value.loadOrCreate();
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

package me.thedivazo.libs.dvzconfig.core.config;

import org.checkerframework.checker.index.qual.PolyIndex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 02.02.2025
 */
public class ConfigContainer {
    private final Map<Class<?>, ConfigWrapper<?>> container;

    private ConfigContainer(Map<Class<?>, ConfigWrapper<?>> container) {
        this.container = Map.copyOf(container);
        container.put(Integer.class, new ConfigWrapper<>(null, null, Integer.class));
    }

    public void loadAll() {
        for (ConfigWrapper<?> value : container.values()) {
            value.loadOrCreate();
        }
        new ConfigContainer(Map.of());
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

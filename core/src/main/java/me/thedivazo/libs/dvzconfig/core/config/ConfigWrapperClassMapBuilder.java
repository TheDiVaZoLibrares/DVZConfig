package me.thedivazo.libs.dvzconfig.core.config;

import me.thedivazo.libs.dvzconfig.core.util.ClassMapBuilder;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public class ConfigWrapperClassMapBuilder extends ClassMapBuilder<ConfigWrapper<?>> {
    public ConfigWrapperClassMapBuilder(Supplier<Map<Class<?>, ConfigWrapper<?>>> supplier) {
        super(supplier);
    }

    public <T> ConfigWrapperClassMapBuilder put(Class<T> clazz, ConfigWrapper<T> wrapper) {
        container.put(clazz, wrapper);
        return this;
    }
}

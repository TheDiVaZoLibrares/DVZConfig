package me.thedivazo.libs.dvzconfig.core.container;

import me.thedivazo.libs.dvzconfig.core.config.ConfigWrapper;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public class ConfigWrapperMutableContainer extends TypeSafeContainer<ConfigWrapper<?>> {
    public ConfigWrapperMutableContainer(Supplier<Map<Class<?>, ConfigWrapper<?>>> supplier) {
        super(supplier);
    }

    public <T> void put(Class<T> clazz, ConfigWrapper<T> wrapper) {
        container.put(clazz, wrapper);
    }
}

package me.thedivazo.libs.dvzconfig.core.util;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 *
 * Чтобы помнять назначение этого класса, рекомендуеться посмотреть его наследников
 */
public abstract class ClassMapBuilder<V> implements MapBuilder<Class<?>, V> {
    protected final Map<Class<?>, V> container;

    protected ClassMapBuilder(Supplier<Map<Class<?>, V>> supplier) {
        this.container = supplier.get();
    }

    public Map<Class<?>, V> builder() {
        return Map.copyOf(container);
    }
}

package me.thedivazo.libs.dvzconfig.core.container;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public abstract class TypeSafeContainer<V> implements ClassToValueContainer<V> {
    protected final Map<Class<?>, V> container;

    protected TypeSafeContainer(Supplier<Map<Class<?>, V>> supplier) {
        this.container = supplier.get();
    }

    public Map<Class<?>, V> getContainer() {
        return container;
    }
}

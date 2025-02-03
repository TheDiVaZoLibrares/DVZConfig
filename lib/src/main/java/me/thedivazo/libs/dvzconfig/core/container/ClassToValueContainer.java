package me.thedivazo.libs.dvzconfig.core.container;

import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public interface ClassToValueContainer<V> {
    Map<Class<?>, V> getContainer();
}

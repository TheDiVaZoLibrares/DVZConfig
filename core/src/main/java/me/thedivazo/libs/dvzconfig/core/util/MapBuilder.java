package me.thedivazo.libs.dvzconfig.core.util;

import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public interface MapBuilder<K, V> {
    Map<Class<?>, V> builder();
}

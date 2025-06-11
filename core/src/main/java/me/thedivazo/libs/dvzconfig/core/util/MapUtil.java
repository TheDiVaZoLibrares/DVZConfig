package me.thedivazo.libs.dvzconfig.core.util;

import java.util.Map;
import java.util.function.BinaryOperator;

public class MapUtil {
    //Copied from java.util.stream.Collector.java
    public static <K, V, M extends Map<K,V>> BinaryOperator<M> uniqKeysMapMerger() {
        return (m1, m2) -> {
            for (Map.Entry<K,V> e : m2.entrySet()) {
                K k = e.getKey();
                if (e.getValue() == null) throw new IllegalArgumentException("Null value");
                V v = e.getValue();
                V u = m1.putIfAbsent(k, v);
                if (u != null) throw new IllegalStateException(String.format(
                        "Duplicate key %s (attempted merging values %s and %s)",
                        k, u, v));
            }
            return m1;
        };
    }
}

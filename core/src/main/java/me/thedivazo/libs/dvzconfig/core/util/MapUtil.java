/*
 * This file is part of DVZConfig, licensed under the Apache License 2.0.
 *
 *  Copyright (c) TheDiVaZo <thedivazoyoutub@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

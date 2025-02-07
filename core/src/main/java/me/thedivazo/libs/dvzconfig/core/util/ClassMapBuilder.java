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

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author TheDiVaZo
 * @since 03.02.2025
 *
 * Чтобы помнять назначение этого класса, рекомендуеться посмотреть его наследников
 */
public abstract class ClassMapBuilder<V extends @NonNull Object> implements MapBuilder<Class<?>, V> {
    protected final Map<Class<?>, V> container;

    protected ClassMapBuilder(Supplier<Map<Class<?>, V>> supplier) {
        this.container = supplier.get();
    }

    public Map<Class<?>, V> builder() {
        return Map.copyOf(container);
    }
}

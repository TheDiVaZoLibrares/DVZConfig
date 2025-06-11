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

package me.thedivazo.libs.dvzconfig.core.serializer;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

public final class ForIdClassSerializer<T, F> implements TypeSerializer<T> {

    private final Class<T> parentClass;
    private final Object[] fieldIdPath;
    private final Class<F> idFieldClass;
    private final BiMap<F, Class<? extends T>> typeClassMap;

    public ForIdClassSerializer(Class<T> parentClass, Object[] fieldIdPath, Class<F> idFieldClass, Map<F, Class<? extends T>> typeClassMap) {
        this.parentClass = parentClass;
        this.fieldIdPath = fieldIdPath;
        this.idFieldClass = idFieldClass;
        this.typeClassMap = ImmutableBiMap.copyOf(typeClassMap);

    }

    public static <T> ForIdClassSerializer<T, String> createSimpleForIdClassSerializer(Class<T> parentClass, String fieldName, Map<String, Class<? extends T>> typeClassMap) {
        return new ForIdClassSerializer<>(parentClass, new Object[]{fieldName}, String.class, typeClassMap);
    }

    @Override
    public T deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!(type instanceof Class<?>) || !parentClass.isAssignableFrom((Class<?>) type)) {
            throw new SerializationException("Expected " + parentClass + " or children, but got " + type);
        }

        F fieldIdValue = node.node(fieldIdPath).get(idFieldClass);
        if (fieldIdValue == null) {
            throw new SerializationException("Could not deserialize object, because exist field id");
        }

        Class<? extends T> clazz = typeClassMap.get(fieldIdValue);

        if (clazz == null) {
            throw new SerializationException("Could not deserialize object, because class for field id not found");
        }
        return ObjectMapper.factory().get(clazz).load(node);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Type type, @Nullable T obj, ConfigurationNode node) throws SerializationException {
        if (obj == null)
            throw new SerializationException("Could not serialize object, because object is null");

        if (!parentClass.isInstance(obj)) {
            throw new SerializationException("Expected instance of " + parentClass.getName() + ", but got " + obj.getClass().getName());
        }

        Optional<F> value = Maps.filterValues(typeClassMap, clazz -> clazz.equals(obj.getClass()) || type instanceof Class<?> typeClazz && typeClazz.isAssignableFrom(obj.getClass()))
                .keySet()
                .stream()
                .findAny();
        if (value.isEmpty()) {
            throw new SerializationException("Could not serialize object, because class object has not registered in serializer");
        }
        node.node(fieldIdPath).set(value.get());
        ObjectMapper.factory().get((Class<T>) obj.getClass()).save(obj, node);
    }
}

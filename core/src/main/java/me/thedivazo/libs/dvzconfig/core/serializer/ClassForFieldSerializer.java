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
import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public final class ClassForFieldSerializer<K, T> implements TypeSerializer<T> {

    private final Ordering<Map.Entry<K, Class<? extends T>>> mapOrdering = new Ordering<>() {
        @Override
        public int compare(Map.Entry<K, Class<? extends T>> left, Map.Entry<K, Class<? extends T>> right) {
            Class<? extends T> leftClass = left.getValue();
            Class<? extends T> rightClass = right.getValue();
            if (leftClass.isAssignableFrom(rightClass)) {
                return 1; // class1 является родителем class2, значит class1 идет ниже
            } else if (rightClass.isAssignableFrom(leftClass)) {
                return -1; // class2 является родителем class1, значит class1 идет выше
            }
            return 0; // Если это один и тот же класс
        }
    };

    private final Object[] fieldIdPath;
    private final Class<K> fieldIdValueClass;
    private final BiMap<K, Class<? extends T>> typeClassMap;

    public ClassForFieldSerializer(Object[] fieldIdPath, Class<K> fieldIdValueClass, Map<K, Class<? extends T>> typeClassMap) {
        this.fieldIdPath = fieldIdPath;
        this.fieldIdValueClass = fieldIdValueClass;
        ImmutableBiMap.Builder<K, Class<? extends T>> builder = ImmutableBiMap.builder();
        for (Map.Entry<K, Class<? extends T>> entry : mapOrdering.sortedCopy(typeClassMap.entrySet())) {
            builder.put(entry.getKey(), entry.getValue());
        }
        this.typeClassMap = builder.build();

    }

    @Override
    public T deserialize(Type type, ConfigurationNode node) throws SerializationException {
        K fieldIdValue = node.node(fieldIdPath).get(fieldIdValueClass);
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
        if (obj == null) {
            throw new SerializationException("Could not serialize object, because object is null");
        }
        Optional<K> idValueOpt = typeClassMap.entrySet().stream().filter(entry -> entry.getValue().isInstance(obj)).map(Map.Entry::getKey).findFirst();
        if (idValueOpt.isEmpty()) {
            throw new SerializationException("Could not serialize object, because id field is null");
        }
        node.node(fieldIdPath).set(idValueOpt.get());
        ((ObjectMapper<T>) ObjectMapper.factory().get(obj.getClass())).save(obj, node);
    }
}

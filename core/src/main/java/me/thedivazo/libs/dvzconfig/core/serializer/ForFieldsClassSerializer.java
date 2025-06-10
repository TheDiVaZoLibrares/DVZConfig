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

import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ForFieldsClassSerializer<T> implements TypeSerializer<T> {
    private final Class<T> parentClass;
    private final Map<Class<? extends T>, Map<String, Class<?>>> classFields;


    public ForFieldsClassSerializer(Class<T> parentClass, Set<Class<? extends T>> classes) {
        this.parentClass = parentClass;
        this.classFields = classes.stream()
                .collect(
                        Collectors.toMap(Function.identity(), clazz ->
                                Arrays.stream(clazz.getFields())
                                        .collect(Collectors.toMap(Field::getName, Field::getType))
                        )
                );
    }

    private static boolean isClass(ConfigurationNode node, Map<String, Class<?>> fields) {
        for (Map.Entry<String, Class<?>> entry : fields.entrySet()) {
            String key = entry.getKey();
            Class<?> expectedClass = entry.getValue();

            ConfigurationNode child = node.node(key);

            if (child.virtual() || child.raw() == null) {
                return false;
            }

            Object rawValue = child.raw();

            if (!expectedClass.isInstance(rawValue)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public T deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!(type instanceof Class<?>) || !parentClass.isAssignableFrom((Class<?>) type)) {
            throw new SerializationException("Expected " + parentClass + " or children, but got " + type);
        }

        Map<Class<? extends T>, Map<String, Class<?>>> findedClass = Maps.filterValues(classFields, fields -> isClass(node, fields));
        if(findedClass.size() != 1) throw new SerializationException("Expected exactly one class, but got " + findedClass.size());

        Class<? extends T> finalyClass = findedClass.keySet().iterator().next();

        return ObjectMapper.factory().get(finalyClass).load(node);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Type type, @Nullable T obj, ConfigurationNode node) throws SerializationException {
        if (obj == null)
            throw new SerializationException("Could not serialize object, because object is null");

        if (!parentClass.isInstance(obj)) {
            throw new SerializationException("Expected instance of " + parentClass.getName() + ", but got " + obj.getClass().getName());
        }

        Class<?> objClass = obj.getClass();

        if (!classFields.containsKey(objClass)) {
            throw new SerializationException("Unknown class for serialization: " + objClass.getName());
        }

        try {
            ObjectMapper<T> mapper = (ObjectMapper<T>) ObjectMapper.factory().get(objClass);
            mapper.save(obj, node);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }
}

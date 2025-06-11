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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import me.thedivazo.libs.dvzconfig.core.util.ReflectionUtil;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ForFieldsClassSerializer<T> implements TypeSerializer<T> {
    private final Ordering<Map.Entry<Class<? extends T>, Set<Field>>> MAP_ORDERING = new Ordering<>() {
        @Override
        public int compare(Map.Entry<Class<? extends T>, Set<Field>> left, Map.Entry<Class<? extends T>, Set<Field>> right) {
            Class<? extends T> leftClass = left.getKey();
            Class<? extends T> rightClass = right.getKey();
            if (leftClass.isAssignableFrom(rightClass)) {
                return 1; // leftClass является родительским классом rightClass – помещаем его ниже
            } else if (rightClass.isAssignableFrom(leftClass)) {
                return -1; // rightClass является родительским классом leftClass – leftClass будет выше
            }
            return 0; // классы равнозначны (например, совпадают)
        }
    };

    public record CustomMapEntry<T>(Class<? extends T> clazz,
                                    Set<Field> values) implements Map.Entry<Class<? extends T>, Set<Field>> {

        @Override
        public Class<? extends T> getKey() {
            return clazz;
        }

        @Override
        public Set<Field> getValue() {
            return values;
        }

        @Override
        public Set<Field> setValue(Set<Field> value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            CustomMapEntry<?> customMapEntry = (CustomMapEntry<?>) o;
            return clazz.equals(customMapEntry.clazz);
        }

        @Override
        public int hashCode() {
            return clazz.hashCode();
        }
    }

    private final Class<T> parentClass;
    private final Map<Class<? extends T>, Set<Field>> classFields;


    public ForFieldsClassSerializer(Class<T> parentClass, Set<Class<? extends T>> classes) {
        this.parentClass = parentClass;
        this.classFields = classes.stream()
                .map(clazz -> new CustomMapEntry<T>(clazz, ReflectionUtil.getAllNoTransientFields(clazz)))
                .sorted(MAP_ORDERING)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toMap(
                                        entry -> entry.getKey(),
                                        (CustomMapEntry<T> entry) -> entry.getValue(),    // лямбда вместо метода
                                        (Set<Field> a, Set<Field> b) -> Sets.union(a, b),
                                        LinkedHashMap::new
                                ),
                                ImmutableMap::copyOf
                        )
                );
    }

    private static boolean isClass(ConfigurationNode node, Set<Field> fields) {
        if (node.childrenMap().size() != fields.size()) return false;
        for (Field field : fields) {
            String key;
            Setting setting = field.getAnnotation(Setting.class);
            if (setting != null) {
                key = setting.value();
            } else {
                key = field.getName();
            }
            ConfigurationNode child = node.node(key);

            if (child.virtual() || child.raw() == null) {
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

        Map<Class<? extends T>, Set<Field>> findClasses = Maps.filterValues(classFields, fields -> isClass(node, fields));
        if (findClasses.size() != 1)
            throw new SerializationException("Expected exactly one class, but got " + findClasses.size());

        Class<? extends T> finalyClass = findClasses.keySet().iterator().next();

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

        Optional<Class<? extends T>> objClass = classFields.keySet().stream().filter(clazz->clazz.isInstance(obj)).findFirst();

        if (objClass.isEmpty()) {
            throw new SerializationException("Unknown class for serialization: " + obj.getClass().getName());
        }

        try {
            ObjectMapper<T> mapper = (ObjectMapper<T>) ObjectMapper.factory().get(objClass.get());
            mapper.save(obj, node);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }
}

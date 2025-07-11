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
import com.google.common.collect.Ordering;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.*;

public final class ForIdClassSerializer<T, F> implements TypeSerializer<T> {

    private final ObjectMapper.Factory mapperFactory;
    private final Class<T> parentClass;
    private final Object[] fieldIdKey;
    private final Class<F> idFieldClass;
    private final BiMap<F, Class<? extends T>> typeClassMap;
    private final Map<Class<? extends T>, TypeSerializer<? extends T>> customSerializers;

    public ForIdClassSerializer(ObjectMapper.Factory factory, Class<T> parentClass, Object[] fieldIdKey, Class<F> idFieldClass, Map<F, Class<? extends T>> typeClassMap, Map<Class<? extends T>, TypeSerializer<? extends T>> customSerializers) {
        this.mapperFactory = factory;
        this.parentClass = parentClass;
        this.fieldIdKey = fieldIdKey;
        this.idFieldClass = idFieldClass;
        ImmutableBiMap.Builder<F, Class<? extends T>> builder = ImmutableBiMap.builder();

        // leftClass является родительским классом rightClass – помещаем его ниже
        // rightClass является родительским классом leftClass – leftClass будет выше
        // классы равнозначны (например, совпадают)
        Ordering<Map.Entry<F, Class<? extends T>>> MAP_ORDERING = new Ordering<>() {
            @Override
            public int compare(Map.Entry<F, Class<? extends T>> left, Map.Entry<F, Class<? extends T>> right) {
                Class<? extends T> leftClass = left.getValue();
                Class<? extends T> rightClass = right.getValue();
                if (leftClass.isAssignableFrom(rightClass)) {
                    return 1; // leftClass является родительским классом rightClass – помещаем его ниже
                } else if (rightClass.isAssignableFrom(leftClass)) {
                    return -1; // rightClass является родительским классом leftClass – leftClass будет выше
                }
                return 0; // классы равнозначны (например, совпадают)
            }
        };

        for (Map.Entry<F, Class<? extends T>> entry : MAP_ORDERING.sortedCopy(typeClassMap.entrySet())) {
            builder.put(entry.getKey(), entry.getValue());
        }
        this.typeClassMap = builder.build();

        this.customSerializers = Map.copyOf(customSerializers);

    }

    public static <T> ForIdClassSerializer<T, String> createSimpleForIdClassSerializer(Class<T> parentClass, String fieldName, Map<String, Class<? extends T>> typeClassMap) {
        return new ForIdClassSerializer<>(ObjectMapper.factory(), parentClass, new Object[]{fieldName}, String.class, typeClassMap, Map.of());
    }

    @Override
    public T deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!(type instanceof Class<?>) || !parentClass.isAssignableFrom((Class<?>) type)) {
            throw new SerializationException(node, type, "Expected " + parentClass + " or children, but got " + type);
        }

        F fieldIdValue = node.node(fieldIdKey).get(idFieldClass);
        if (fieldIdValue == null) {
            throw new SerializationException(node, type, "Could not deserialize object, because exist field id");
        }

        Class<? extends T> clazz = typeClassMap.get(fieldIdValue);

        if (clazz == null) {
            throw new SerializationException(node, type, "Could not deserialize object, because class for field id not found");
        }

        Optional<TypeSerializer<? extends T>> serializer = Optional.ofNullable(customSerializers.get(clazz));

        if (serializer.isPresent()) {
            return serializer.get().deserialize(type, node);
        }
        else {
            return mapperFactory.get(clazz).load(node);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Type type, @Nullable T obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            final ConfigurationNode id = node.node(fieldIdKey);
            node.set(null);
            if (!id.virtual()) {
                node.node(fieldIdKey).set(id);
            }
            return;
        }

        if (!parentClass.isInstance(obj)) {
            throw new SerializationException(node, type, "Expected instance of " + parentClass.getName() + ", but got " + obj.getClass().getName());
        }

        Optional<F> value = Maps.filterValues(typeClassMap, clazz -> clazz.isInstance(obj))
                .keySet()
                .stream()
                .findAny();
        if (value.isEmpty()) {
            throw new SerializationException(node, type, "Could not serialize object, because class object has not registered in serializer");
        }

        Class<T> clazz = (Class<T>) typeClassMap.get(value.get());

        if (clazz == null) {
            throw new SerializationException(node, type, "Could not serialize object, because class object has not registered in serializer");
        }

        node.node(fieldIdKey).set(value.get());

        TypeSerializer<T> serializer = (TypeSerializer<T>) customSerializers.get(clazz);

        if (serializer != null) {
            serializer.serialize(type, obj, node);
        }
        else {
            mapperFactory.get(clazz).save(obj, node);
        }
    }

    /* -------- BUILDER ---------- */

    /**
     * Fluent builder for {@link ForIdClassSerializer}.
     *
     * <p>Usage:</p>
     *
     * <pre>
     * ForIdClassSerializer<MyBase, String> serializer = ForIdClassSerializer.builder(MyBase.class, String.class)
     *         .fieldPath("type")                // path to the id field in the config
     *         .register("cat", Cat.class)       // id -> impl
     *         .register("dog", Dog.class)
     *         .registerAll(extraMap)            // you can still dump a whole map if you have one
     *         .build();
     * </pre>
     *
     * @param <T> base type of all serialised classes
     * @param <F> type of the discriminator field (the id)
     */
    public static final class Builder<T, F> {

        /* ===== required ===== */
        private final Class<T> parentClass;
        private final Class<F> idFieldClass;

        /* ===== optional / incremental ===== */
        private ObjectMapper.Factory mapperFactory = ObjectMapper.factory();
        private final List<Object> fieldIdPath = new ArrayList<>();
        private final Map<F, Class<? extends T>> typeClassMap = new LinkedHashMap<>();

        private final Map<Class<? extends T>, TypeSerializer<? extends T>> customSerializers = new LinkedHashMap<>();

        private Builder(@NonNull Class<T> parentClass, @NonNull Class<F> idFieldClass) {
            this.parentClass = parentClass;
            this.idFieldClass = idFieldClass;
        }

        // ---------- factory entry point ----------
        public static <T, F> Builder<T, F> create(Class<T> parentClass, Class<F> idFieldClass) {
            return new Builder<>(parentClass, idFieldClass);
        }

        // or the 👇 convenience alias you’ll actually call
        public static <T, F> Builder<T, F> builder(Class<T> parentClass, Class<F> idFieldClass) {
            return create(parentClass, idFieldClass);
        }

        /** Accept raw Configurate path parts (mixed types allowed). */
        public Builder<T, F> fieldPath(Object... path) {
            fieldIdPath.clear();
            fieldIdPath.addAll(Arrays.asList(path));
            return this;
        }

        /** Convenience overload for single-segment string paths. */
        public Builder<T, F> fieldPath(String single) {
            return fieldPath((Object) single);
        }

        public Builder<T, F> mapperFactory(ObjectMapper.Factory factory) {
            if (factory != null) {
                this.mapperFactory = factory;
            }
            return this;
        }

        public Builder<T, F> registerClass(F id, Class<? extends T> clazz) {
            typeClassMap.put(
                    Objects.requireNonNull(id, "id"),
                    Objects.requireNonNull(clazz, "clazz"));
            return this;
        }

        public Builder<T, F> registerClasses(Map<F, Class<? extends T>> mappings) {
            mappings.forEach(this::registerClass);
            return this;
        }

        public Builder<T, F> registerSerializer(Class<? extends T> clazz, TypeSerializer<? extends T> typeSerializer) {
            customSerializers.put(
                    Objects.requireNonNull(clazz, "id"),
                    Objects.requireNonNull(typeSerializer, "typeSerializer"));
            return this;
        }

        public Builder<T, F> registerSerializers(Map<Class<? extends T>, TypeSerializer<? extends T>> typeSerializerMap) {
            typeSerializerMap.forEach(this::registerSerializer);
            return this;
        }

        // ---------- finish him ----------
        public ForIdClassSerializer<T, F> build() {
            if (fieldIdPath.isEmpty()) {
                throw new IllegalStateException("fieldIdPath is required but not set");
            }
            if (typeClassMap.isEmpty()) {
                throw new IllegalStateException("No id↔class mappings registered");
            }
            return new ForIdClassSerializer<>(
                    mapperFactory,
                    parentClass,
                    fieldIdPath.toArray(),
                    idFieldClass,
                    typeClassMap,
                    customSerializers
            );
        }
    }
}

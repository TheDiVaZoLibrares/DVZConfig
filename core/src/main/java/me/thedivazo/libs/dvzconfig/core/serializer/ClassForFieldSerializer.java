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
 * Сериализатор и десериализатор для иерархических (наследуемых) классов.
 *
 * <p>Данный класс позволяет корректно (де)сериализовывать объекты, являющиеся экземплярами классов,
 * образующих иерархию наследования, используя специальное поле конфигурации для определения точного типа
 * объекта.</p>
 *
 * <H2>Описание работы</H2>
 * <p>Для корректной работы сериализатора необходимо при создании экземпляра передать следующие параметры:</p>
 * <ul>
 *   <li>
 *     <b>Путь к полю идентификатора типа</b> (<code>Object[] fieldIdPath</code>):
 *     определяет путь в конфигурационном узле, где хранится значение, указывающее тип объекта.
 *   </li>
 *   <li>
 *     <b>Класс значения идентификатора</b> (<code>Class&lt;K&gt; fieldIdValueClass</code>):
 *     тип значения, извлекаемого по указанному пути (например, {@code String}).
 *   </li>
 *   <li>
 *     <b>Карта сопоставления идентификаторов и классов</b> (<code>Map&lt;K, Class&lt;? extends T&gt;&gt; typeClassMap</code>):
 *     отображение, в котором каждому значению идентификатора соответствует конкретный класс,
 *     подлежащий (де)сериализации.
 *   </li>
 * </ul>
 *
 * <H2>Пример использования</H2>
 * <p>Рассмотрим следующую иерархию классов:</p>
 *
 * <pre>{@code
 * @AllArgsConstructor
 * public class Param {
 *     String param1;
 * }
 *
 * @AllArgsConstructor
 * public class ParamA extends Param {
 *     int param2;
 * }
 *
 * @AllArgsConstructor
 * public class ParamB extends Param {
 *     boolean param3;
 * }
 *
 * @AllArgsConstructor
 * public class ParamAC extends ParamA {
 *     char param4;
 * }
 * }</pre>
 *
 * <p>Затем создаём карту сопоставления, где ключом является идентификатор (например, {@code String}),
 * а значением – соответствующий класс:</p>
 *
 * <pre>{@code
 * Map<Class<? extends Param>, String> typeMap = Map.of(
 *     ParamA.class, "paramA"
 *     ParamB.class, "paramB",
 *     ParamAC.class, "paramAC"
 * );
 *
 * // Указываем, что поле "type" в конфигурационном узле содержит идентификатор типа
 * ClassForFieldSerializer<String, Param> serializer =
 *     new ClassForFieldSerializer<>(new Object[]{"type"}, String.class, typeMap);
 * }</pre>
 *
 * <H2>Процесс десериализации</H2>
 * <ol>
 *   <li>Из конфигурационного узла по пути {@code fieldIdPath} извлекается значение идентификатора.</li>
 *   <li>На основании этого значения определяется соответствующий класс из карты.</li>
 *   <li>Далее, с помощью {@link ObjectMapper} происходит десериализация узла в объект нужного класса.</li>
 * </ol>
 *
 * <H2>Процесс сериализации</H2>
 * <ol>
 *   <li>Проверяется, что сериализуемый объект не равен {@code null}.</li>
 *   <li>Определяется идентификатор типа, сопоставленный с классом объекта, посредством поиска в карте.</li>
 *   <li>Записывается значение идентификатора в конфигурационный узел по указанному пути.</li>
 *   <li>С помощью {@link ObjectMapper} объект сохраняется в узле конфигурации.</li>
 * </ol>
 *
 * <p>Если объект не соответствует ни одному из классов, указанных в карте, или отсутствует требуемый идентификатор в конфигурации,
 * методы сериализации/десериализации выбрасывают {@link SerializationException}.</p>
 *
 * <H2>Пример интеграции с конфигурацией</H2>
 * <pre>{@code
 * // Класс конфигурации
 * @ConfigSerializable
 * public class Config {
 *     Param typeParam = new ParamA("any value", 35);
 * }
 *
 * // Пример YAML файла (config.yml):
 * typeParam:
 *   type: paramA
 *   param: "any value"
 *   param2: 35
 * }</pre>
 *
 * @param <T> базовый тип объектов, которые могут быть (де)сериализованы данным сериализатором
 * @param <K> тип идентификатора (например, {@code String}). Рекомендуется использовать Immutable тип.
 *
 * @see TypeSerializer
 * @see ObjectMapper
 *
 * @author TheDiVaZo
 * @since 31.01.2025
 */
public final class ClassForFieldSerializer<T, K> implements TypeSerializer<T> {

    private final Ordering<Map.Entry<Class<? extends T>, K>> mapOrdering = new Ordering<>() {
        @Override
        public int compare(Map.Entry<Class<? extends T>, K> left, Map.Entry<Class<? extends T>, K> right) {
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

    private final Object[] fieldIdPath;
    private final Class<K> fieldIdValueClass;
    private final BiMap<Class<? extends T>, K> typeClassMap;

    /**
     * Конструктор, инициализирующий сериализатор с указанными параметрами.
     *
     * @param fieldIdPath      путь в конфигурационном узле до поля, содержащего идентификатор типа
     * @param fieldIdValueClass класс значения идентификатора (например, {@code String.class})
     * @param typeClassMap     карта сопоставления идентификаторов и классов, подлежащих (де)сериализации
     */
    public ClassForFieldSerializer(Object[] fieldIdPath, Class<K> fieldIdValueClass, Map<Class<? extends T>, K> typeClassMap) {
        this.fieldIdPath = fieldIdPath;
        this.fieldIdValueClass = fieldIdValueClass;
        ImmutableBiMap.Builder<Class<? extends T>, K> builder = ImmutableBiMap.builder();
        for (Map.Entry<Class<? extends T>, K> entry : mapOrdering.sortedCopy(typeClassMap.entrySet())) {
            builder.put(entry.getKey(), entry.getValue());
        }
        this.typeClassMap = builder.build();

    }

    /**
     * Десериализует объект из указанного конфигурационного узла.
     *
     * <p>Процесс десериализации выполняется следующим образом:
     * <ol>
     *   <li>Из узла по пути {@code fieldIdPath} извлекается значение идентификатора.</li>
     *   <li>Определяется класс объекта, соответствующий данному идентификатору из {@code typeClassMap}.</li>
     *   <li>С помощью {@link ObjectMapper} происходит загрузка объекта из узла.</li>
     * </ol>
     *
     *
     * @param type тип объекта (не используется непосредственно, но требуется интерфейсом)
     * @param node конфигурационный узел, из которого производится десериализация
     * @return десериализованный объект типа {@code T}
     * @throws SerializationException если не удалось найти идентификатор или соответствующий класс, либо при ошибке десериализации
     */
    @Override
    public T deserialize(Type type, ConfigurationNode node) throws SerializationException {
        K fieldIdValue = node.node(fieldIdPath).get(fieldIdValueClass);
        if (fieldIdValue == null) {
            throw new SerializationException("Could not deserialize object, because exist field id");
        }
        Class<? extends T> clazz = typeClassMap.inverse().get(fieldIdValue);
        if (clazz == null) {
            throw new SerializationException("Could not deserialize object, because class for field id not found");
        }
        return ObjectMapper.factory().get(clazz).load(node);
    }

    /**
     * Сериализует объект в указанный конфигурационный узел.
     *
     * <p>Процесс сериализации включает следующие шаги:
     * <ol>
     *   <li>Проверяется, что объект не {@code null}.</li>
     *   <li>Поиск в {@code typeClassMap} идентификатора, сопоставленного с классом объекта.</li>
     *   <li>Запись идентификатора в узел по пути {@code fieldIdPath}.</li>
     *   <li>Сохранение объекта в узле с помощью {@link ObjectMapper}.</li>
     * </ol>
     *
     * @param type тип объекта (не используется непосредственно, но требуется интерфейсом)
     * @param obj  объект для сериализации; не должен быть {@code null}
     * @param node конфигурационный узел, в который производится сериализация
     * @throws SerializationException если объект равен {@code null} или не удалось определить идентификатор для сериализации
     */
    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Type type, @Nullable T obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Could not serialize object, because object is null");
        }
        Optional<K> idValueOpt = typeClassMap.entrySet().stream().filter(entry -> entry.getKey().isInstance(obj)).map(Map.Entry::getValue).findFirst();
        if (idValueOpt.isEmpty()) {
            throw new SerializationException("Could not serialize object, because id field is null");
        }
        node.node(fieldIdPath).set(idValueOpt.get());
        ((ObjectMapper<T>) ObjectMapper.factory().get(obj.getClass())).save(obj, node);
    }
}

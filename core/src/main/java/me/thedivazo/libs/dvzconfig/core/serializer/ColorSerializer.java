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

import com.google.common.base.Strings;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;

/**
 * Абстрактный класс для сериализации и десериализации объектов, представляющих цвет.
 * <p>
 * По умолчанию данный сериализатор преобразует объект цвета в строку в формате {@code #rrggbb}
 * или {@code #aarrggbb} (если значение альфа-канала ненулевое). Формат десериализации совпадает с форматом,
 * поддерживаемым методом {@link Integer#decode(String)}.
 * </p>
 * <p>
 * Кроме числового представления, данный класс поддерживает десериализацию по названию цвета. Для этого в конструктор
 * передаётся {@code Map<String, Integer>}, сопоставляющая название цвета с его числовым значением (ARGB).
 * </p>
 * @param <C> Класс, представляющий цвет
 * @author TheDiVaZo
 * @since 31.01.2025
 */
public abstract class ColorSerializer<C> extends ScalarSerializer<C> {
    protected final Map<String, Integer> colorNamesToArgbMap;

    protected ColorSerializer(TypeToken<C> type, Map<String, Integer> colorNamesToArgbMap) {
        super(type);
        this.colorNamesToArgbMap = Map.copyOf(colorNamesToArgbMap);
    }

    protected OptionalInt deserialize(String argb) {
        try {
            return OptionalInt.of(Integer.decode(argb));
        } catch (NumberFormatException ignored) {
            return OptionalInt.empty();
        }
    }

    protected String serialize(int argb) {
        final char HEX_CHARACTER = '#';
        final int rgbLength = 6;
        final int argbLength = 8;

        String hex = Integer.toHexString(argb);
        int minLength = hex.length() <= rgbLength ? rgbLength : argbLength;

        return HEX_CHARACTER + Strings.padEnd(Integer.toHexString(argb), minLength, '0');
    }

    @Override
    public C deserialize(Type type, Object obj) throws SerializationException {
        String str = obj.toString();
        OptionalInt optionalInt = deserialize(str);
        Integer argbOrNull = null;
        if (optionalInt.isPresent()) {
            argbOrNull = optionalInt.getAsInt();
        } else {
            argbOrNull = colorNamesToArgbMap.get(str);
        }

        int argb = Optional
                .ofNullable(argbOrNull)
                .orElseThrow(() -> new SerializationException("Could not deserialize color from string: " + str));
        return fromARGB(argb);
    }
    @Override
    protected Object serialize(C item, Predicate<Class<?>> typeSupported) {
        int argb = toARGB(item);
        return serialize(argb);
    }

    /**
     * Преобразует целочисленное значение ARGB в объект цвета.
     * <b><b></b></b>
     * Формат представления цвета в числе <code>argb</code> следующий:
     * <ul>
     *   <li><code>(argb >> 24) &amp; 0xFF</code> — альфа-канал (прозрачность)</li>
     *   <li><code>(argb >> 16) &amp; 0xFF</code> — красный канал</li>
     *   <li><code>(argb >> 8) &amp; 0xFF</code> — зеленый канал</li>
     *   <li><code>(argb) &amp; 0xFF</code> — синий канал</li>
     * </ul>
     * При сериализации, если альфа-канал равен 0, он может быть опущен, и используется формат <code>#rrggbb</code>,
     * в противном случае — формат <code>#aarrggbb</code>.
     *
     * @param argb целочисленное значение цвета в формате ARGB
     * @return объект цвета, созданный на основе переданного значения
     */
    protected abstract C fromARGB(int argb);
    protected abstract int toARGB(C color);
}

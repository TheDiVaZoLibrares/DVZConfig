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
import java.util.stream.Collectors;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 * <p>
 * Цвет может быть представлен в двух форматах:
 * Строка с HEX числом -> #123456, 0x123456
 * Либо в текстовом формате названия цвета -> red, green, blue
 */
public abstract class ColorSerializer<C> extends ScalarSerializer<C> {
    private static final char HEX_CHARACTER = '#';

    protected final Map<String, Integer> colorNamesModel;
    protected final Map<Integer, C> cachedColors;

    protected ColorSerializer(TypeToken<C> type, Map<String, Integer> colorNamesModel) {
        super(type);
        this.colorNamesModel = Map.copyOf(colorNamesModel);
        this.cachedColors = colorNamesModel.values().stream()
                .map(integer -> Map.entry(integer, fromRgba(integer)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected OptionalInt optionalDecode(String str) {
        try {
            return OptionalInt.of(Integer.decode(str));
        } catch (NumberFormatException ignored) {
            return OptionalInt.empty();
        }
    }

    @Override
    public C deserialize(Type type, Object obj) throws SerializationException {
        String str = obj.toString();
        OptionalInt optionalInt = optionalDecode(str);
        Integer rgbaOrNull = null;
        if (optionalInt.isPresent()) {
            rgbaOrNull = optionalInt.getAsInt();
        } else {
            rgbaOrNull = colorNamesModel.get(str);
        }

        int rgba = Optional
                .ofNullable(rgbaOrNull)
                .orElseThrow(() -> new SerializationException("Could not deserialize color from string: " + str));
        return Optional
                .ofNullable(cachedColors.get(rgba))
                .orElseGet(()-> fromRgba(rgba));
    }
    @Override
    protected Object serialize(C item, Predicate<Class<?>> typeSupported) {
        final int rgbLength = 6;
        final int rgbaLength = 8;

        int rgba = fromColor(item);
        String hex = Integer.toHexString(rgba);
        int minLength = hex.length() <= rgbLength ? rgbLength : rgbaLength;

        return HEX_CHARACTER + Strings.padEnd(Integer.toHexString(rgba), minLength, '0');
    }

    protected abstract C fromRgba(int rgba);
    protected abstract int fromColor(C color);
}

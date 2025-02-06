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
 * @author TheDiVaZo
 * created on 31.01.2025
 * <p>
 * Цвет может быть представлен в двух форматах:
 * Строка с HEX числом -> #123456, 0x123456
 * Либо в текстовом формате названия цвета -> red, green, blue
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
     * i << 24 - alpha
     * i << 16 - red
     * i << 8 - green
     * i << 0 - blue
     * @param argb
     * @return
     */
    protected abstract C fromARGB(int argb);
    protected abstract int toARGB(C color);
}

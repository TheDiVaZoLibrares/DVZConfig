package me.thedivazo.libs.dvzconfig.core.serializer;

import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 *
 * Цвет может быть представлен в двух форматах:
 * Строка с HEX числом -> #123456, 0x123456
 * Либо в текстовом формате названия цвета -> red, green, blue
 */
public abstract class ColorSerializer<C> extends ScalarSerializer<C> {

    protected final Map<String, Integer> colorNamesModel;

    protected ColorSerializer(TypeToken<C> type, Map<String, Integer> colorNamesModel) {
        super(type);
        this.colorNamesModel = Map.copyOf(colorNamesModel);
    }

    protected final OptionalInt optionalDecode(String str) {
        try {
            return OptionalInt.of(Integer.decode(str));
        } catch (NumberFormatException ignored){
            return OptionalInt.empty();
        }
    }

    @Override
    public C deserialize(Type type, Object obj) throws SerializationException {
        String str = obj.toString();
        OptionalInt optionalInt = optionalDecode(str);
        if (optionalInt.isPresent()) {
            return fromRgb(optionalInt.getAsInt());
        }
        else {
            Integer colorAsRgb = colorNamesModel.get(str);
            if (colorAsRgb!= null) {
                return fromRgb(colorAsRgb);
            }
            else {
                throw new SerializationException("Could not deserialize color from string: " + str);
            }
        }
    }

    @Override
    protected Object serialize(C item, Predicate<Class<?>> typeSupported) {
        return null;
    }

    protected abstract C fromRgb(int rgb);
}

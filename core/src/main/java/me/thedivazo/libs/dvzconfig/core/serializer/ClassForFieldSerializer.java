package me.thedivazo.libs.dvzconfig.core.serializer;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public class ClassForFieldSerializer<K, T> implements TypeSerializer<T> {
    private final Object[] fieldIdPath;
    private final Class<K> fieldIdValueClass;
    private final Function<T, K> fieldIdValueExtractor;
    private final Map<K, Class<? extends T>> fieldValueToClass;

    public ClassForFieldSerializer(Object[] fieldIdPath, Class<K> fieldIdValueClass, Function<T, K> fieldIdValueExtractor, Map<K, Class<? extends T>> fieldValueToClass) {
        this.fieldIdPath = fieldIdPath;
        this.fieldIdValueClass = fieldIdValueClass;
        this.fieldIdValueExtractor = fieldIdValueExtractor;
        this.fieldValueToClass = Map.copyOf(fieldValueToClass);
    }

    @Override
    public T deserialize(Type type, ConfigurationNode node) throws SerializationException {
        K fieldIdValue = node.node(fieldIdPath).get(fieldIdValueClass);
        if (fieldIdValue == null) {
            throw new SerializationException("Could not deserialize object, because exist field id");
        }
        Class<? extends T> clazz = fieldValueToClass.get(fieldIdValue);
        if (clazz == null) {
            throw new SerializationException("Could not deserialize object, because class for field id not found");
        }
        return ObjectMapper.factory().get(clazz).load(node);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Type type, T obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Could not serialize object, because object is null");
        }
        Class<? extends T> clazz = fieldValueToClass.get(fieldIdValueExtractor.apply(obj));
        if (clazz == null) {
            throw new SerializationException("Could not deserialize object, because class for field id not found");
        }
        if (!clazz.isInstance(obj)) {
            throw new SerializationException("Could not serialize object, because object is not instance of expected class");
        }
        ((ObjectMapper<T>) ObjectMapper.factory().get(clazz)).save(obj, node);
    }
}

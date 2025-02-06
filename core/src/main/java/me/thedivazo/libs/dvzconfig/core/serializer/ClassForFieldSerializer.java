package me.thedivazo.libs.dvzconfig.core.serializer;

import com.google.common.collect.*;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public final class ClassForFieldSerializer<K, T> implements TypeSerializer<T> {
    private final Object[] fieldIdPath;
    private final Class<K> fieldIdValueClass;
    private final BiMap<K, Class<? extends T>> typeClassMap;

    public ClassForFieldSerializer(Object[] fieldIdPath, Class<K> fieldIdValueClass, Map<K, Class<? extends T>> typeClassMap) {
        this.fieldIdPath = fieldIdPath;
        this.fieldIdValueClass = fieldIdValueClass;
        this.typeClassMap = ImmutableBiMap.copyOf(typeClassMap);
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
    public void serialize(Type type, T obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Could not serialize object, because object is null");
        }
        K idValue = typeClassMap.inverse().get(obj.getClass());
        if (idValue == null) {
            throw new SerializationException("Could not serialize object, because id field is null");
        }
        node.node(fieldIdPath).set(idValue);
        ((ObjectMapper<T>) ObjectMapper.factory().get(obj.getClass())).save(obj, node);
    }
}

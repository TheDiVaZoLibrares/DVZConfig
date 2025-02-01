package me.thedivazo.libs.dvzconfig.paper.serializer;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

//TODO: Реализовать сериализацию компонента. На этом можно и закончить.
public class ComponentSerializer implements TypeSerializer<Component> {
    @Override
    public Component deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return null;
    }

    @Override
    public void serialize(Type type, @Nullable Component obj, ConfigurationNode node) throws SerializationException {
        return;
    }
}

package me.thedivazo.libs.dvzconfig.paper.serializer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.function.Predicate;

/**
 * https://docs.advntr.dev/minimessage/format.html#minimessage-format
 */
public class ComponentSerializer extends ScalarSerializer<Component> {
    public static final ComponentSerializer DEFAULT =  new ComponentSerializer(MiniMessage.miniMessage());

    private final MiniMessage miniMessage;

    protected ComponentSerializer(MiniMessage miniMessage) {
        super(Component.class);
        this.miniMessage = miniMessage;
    }

    @Override
    public Component deserialize(Type type, Object obj) throws SerializationException {
        return miniMessage.deserialize(obj.toString());
    }

    @Override
    protected Object serialize(Component item, Predicate<Class<?>> typeSupported) {
        return miniMessage.serialize(item);
    }
}

package me.thedivazo.libs.dvzconfig.paper.serializer;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.serialize.CoercionFailedException;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * @author TheDiVaZo
 * created on 01.02.2025
 */
public class PotionEffectTypeSerializer extends ScalarSerializer<PotionEffectType> {
    public static PotionEffectTypeSerializer DEFAULT = new PotionEffectTypeSerializer();

    public PotionEffectTypeSerializer() {
        super(PotionEffectType.class);
    }

    @Override
    public PotionEffectType deserialize(Type type, Object value) throws SerializationException {
        final String potential = value.toString().toLowerCase(Locale.ROOT);

        PotionEffectType potionEffectType = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(potential));
        if (potionEffectType == null) throw new CoercionFailedException(type, value, "potion_effect_type");
        return potionEffectType;
    }

    @Override
    protected Object serialize(PotionEffectType item, Predicate<Class<?>> typeSupported) {
        return item.getKey().value();
    }
}

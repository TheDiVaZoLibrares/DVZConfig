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

package me.thedivazo.libs.dvzconfig.paper.serializer;

import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.serialize.CoercionFailedException;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * @author TheDiVaZo
 * @since 01.02.2025
 */
public class PotionEffectTypeSerializer extends ScalarSerializer<PotionEffectType> {
    public static final PotionEffectTypeSerializer DEFAULT = new PotionEffectTypeSerializer();

    public PotionEffectTypeSerializer() {
        super(PotionEffectType.class);
    }

    @Override
    public PotionEffectType deserialize(Type type, Object value) throws SerializationException {
        final String potential = value.toString().toLowerCase(Locale.ROOT);

        PotionEffectType potionEffectType = PotionEffectType.getByName(potential);
        if (potionEffectType == null) throw new CoercionFailedException(type, value, "potion_effect_type");
        return potionEffectType;
    }

    @Override
    protected Object serialize(PotionEffectType item, Predicate<Class<?>> typeSupported) {
        return item.getName();
    }
}

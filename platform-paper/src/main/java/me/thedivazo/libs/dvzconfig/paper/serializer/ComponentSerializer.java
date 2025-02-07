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

    public ComponentSerializer(MiniMessage miniMessage) {
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

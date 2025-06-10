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

package me.thedivazo.libs.dvzconfig.core.serializer;

import me.thedivazo.libs.dvzconfig.Util;
import me.thedivazo.libs.dvzconfig.object.Reward;
import me.thedivazo.libs.dvzconfig.object.RewardEffect;
import me.thedivazo.libs.dvzconfig.object.RewardItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ForFieldsClassSerializerTest {
    private static ForFieldsClassSerializer<Reward> rewardSerializer;

    @BeforeAll
    static void setUp() {
        rewardSerializer = new ForFieldsClassSerializer<>(
                Reward.class,
                Set.of(RewardEffect.class, RewardItem.class)
        );
    }

    @Test
    void testDeserializeRewardEffect() throws SerializationException {
        ConfigurationNode node = Util.createNode("""
            effects:
              - SPEED
              - STRENGTH
            durable: 60.0
        """);

        Reward reward = rewardSerializer.deserialize(Reward.class, node);
        assertTrue(reward instanceof RewardEffect);

        RewardEffect effect = (RewardEffect) reward;
        assertEquals(List.of("SPEED", "STRENGTH"), effect.getEffects());
        assertEquals(60.0, effect.getDurable());
    }

    @Test
    void testDeserializeRewardItem() throws SerializationException {
        ConfigurationNode node = Util.createNode("""
            itemsIdAndCounts:
              sword: 1
              apple: 5
        """);

        Reward reward = rewardSerializer.deserialize(Reward.class, node);
        assertTrue(reward instanceof RewardItem);

        RewardItem item = (RewardItem) reward;
        assertEquals(Map.of("sword", 1, "apple", 5), item.getItemsIdAndCounts());
    }

    @Test
    void testSerializeRewardEffect() throws SerializationException {
        RewardEffect effect = new RewardEffect(List.of("JUMP"), 30.5);

        ConfigurationNode node = YamlConfigurationLoader.builder().build().createNode();
        rewardSerializer.serialize(Reward.class, effect, node);

        assertEquals(List.of("JUMP"), node.node("effects").getList(String.class));
        assertEquals(30.5, node.node("durable").getDouble());
    }

    @Test
    void testSerializeRewardItem() throws SerializationException {
        RewardItem item = new RewardItem(Map.of("diamond", 3));

        ConfigurationNode node = YamlConfigurationLoader.builder().build().createNode();
        rewardSerializer.serialize(Reward.class, item, node);

        assertEquals(3, node.node("itemsIdAndCounts", "diamond").getInt());
    }

    @Test
    void testDeserializeFailsWhenNoMatch() {
        ConfigurationNode node = Util.createNode("""
            unknownField: 42
        """);

        assertThrows(SerializationException.class, () ->
                rewardSerializer.deserialize(Reward.class, node)
        );
    }

    @Test
    void testDeserializeFailsWhenMultipleMatches() {
        ConfigurationNode node = Util.createNode("""
            effects:
              - SPEED
            durable: 60.0
            itemsIdAndCounts:
              sword: 1
        """);

        assertThrows(SerializationException.class, () ->
                rewardSerializer.deserialize(Reward.class, node)
        );
    }
}

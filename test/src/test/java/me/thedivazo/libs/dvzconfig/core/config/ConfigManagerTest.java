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

package me.thedivazo.libs.dvzconfig.core.config;

import me.thedivazo.libs.dvzconfig.core.serializer.ClassForFieldSerializer;
import me.thedivazo.libs.dvzconfig.object.*;
import me.thedivazo.libs.dvzconfig.yaml.YamlConfigLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigManagerTest {

    private static YamlConfigLoader<TestConfigOne> yamlConfigLoader;
    private static ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private static ClassForFieldSerializer<String, Animal> animalNameSerializer;
    private static ConfigContainer container;
    private static Path path;

    @BeforeAll
    static void setUp() throws URISyntaxException {
        path = Paths.get(ConfigManagerImpl.class.getClassLoader().getResource("").toURI()).resolve("testconfig.yml");

        System.out.println(path);

        animalNameSerializer = new ClassForFieldSerializer<>(
                new Object[]{"type"},
                String.class,
                Map.of(
                        "animal", Animal.class,
                        "bug", Bug.class,
                        "cat", Cat.class,
                        "cow", Cow.class
                )
        );

        yamlConfigLoader = new YamlConfigLoader<>(
                NodeStyle.BLOCK, 4, TypeSerializerCollection.defaults().childBuilder().register(Animal.class, animalNameSerializer).build()
        );

        configurationLoader = yamlConfigLoader.getLoader(path);

        container = new ConfigContainer(Set.of(new ConfigWrapper<>(path, yamlConfigLoader, new TestConfigOne())));
    }

    @BeforeEach
    @AfterEach
    void removeFile() throws IOException {
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    void testLoadConfig() throws IOException {
        ConfigManagerImpl configManager = new ConfigManagerImpl(container);
        configManager.load();
        assertTrue(Files.exists(path));
        assertTrue(Files.size(path) > 0);

        ConfigurationNode configNode = configurationLoader.load();
        TestConfigOne actualConfig = configManager.getConfigContainer().getConfig(TestConfigOne.class).getConfig();
        // Проверка значений
        assertEquals(configNode.node("zoo-name").getString(), actualConfig.getZooName());

        // Сравнение объекта cow
        assertEquals(configNode.node("cow", "name").getString(), actualConfig.getCow().getName());
        assertEquals(configNode.node("cow", "age").getInt(), actualConfig.getCow().getAge());
        assertEquals(configNode.node("cow", "is-pregnant").getBoolean(), actualConfig.getCow().isPregnant());

        // Сравнение объектов в map
        assertEquals(configNode.node("map", "papapa", "name").getString(), actualConfig.getMap().get("papapa").getName());
        assertEquals(configNode.node("map", "papapa", "age").getInt(), actualConfig.getMap().get("papapa").getAge());
        assertEquals(configNode.node("map", "papapa", "is-pregnant").getBoolean(),
                ((Cow) actualConfig.getMap().get("papapa")).isPregnant());

        assertEquals(configNode.node("map", "pepepe", "name").getString(), actualConfig.getMap().get("pepepe").getName());
        assertEquals(configNode.node("map", "pepepe", "age").getInt(), actualConfig.getMap().get("pepepe").getAge());
        assertEquals(configNode.node("map", "pepepe", "color").getString(),
                ((Cat) actualConfig.getMap().get("pepepe")).getColor());

    }
}
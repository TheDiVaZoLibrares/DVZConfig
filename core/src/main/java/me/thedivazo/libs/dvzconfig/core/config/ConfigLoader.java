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

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * Класс, отвечающий за загрузку и сохранение конфига
 *
 * @author TheDiVaZo
 * @since 31.01.2025
 */
public abstract class ConfigLoader {
    protected final TypeSerializerCollection[] serializerCollections;

    /**
     * @param serializerCollections список дополнительный сериализаторов.
     */
    protected ConfigLoader(TypeSerializerCollection[] serializerCollections) {
        this.serializerCollections = Arrays.copyOf(serializerCollections, serializerCollections.length);
    }

    /**
     * Загружает конфигурацию из файла и десириализует ее.
     * Если файла нет, возвращает конфигурацию со значениями по умолчанию.
     * В зависимости от параметра {@code save} сохраняет загруженный или созданный по умолчанию конфиг в файл или нет
     *
     * @param pathToFile путь до файла
     * @param save нужно ли сохранить конфиг в файле.
     * @return десериализованный объект класса конфига из файла или конфиг по умолчанию
     */
    public <T> T load(Path pathToFile, Class<? extends T> clazz, boolean save) {
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = getLoader(pathToFile);
            CommentedConfigurationNode node = loader.load();
            T config = node.get(clazz);
            if (config == null) {
                throw new IllegalArgumentException("Invalid config deserialize or invalid path");
            }
            if (save) {
                node.set(clazz, config);
                loader.save(node);
            }
            return config;
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void save(Path pathToFile, T config) {
        if (config == null) {
            throw new IllegalArgumentException("default config is null");
        }
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = getLoader(pathToFile);
            CommentedConfigurationNode node = loader.load();
            node.set(config.getClass(), config);
            loader.save(node);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает загрузчик конфигурации из фреймворка  `Configurate`
     *
     * @param pathToFile путь до файла
     * @return загрузчик конфигурации
     */
    public abstract ConfigurationLoader<CommentedConfigurationNode> getLoader(Path pathToFile);
}

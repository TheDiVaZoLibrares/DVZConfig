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

import me.thedivazo.libs.dvzconfig.core.util.ReflectionUtil;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;

/**
 * Класс, инкапсулирующий в себе низкоуровневую работу с конкретным конфигом
 * Данный класс не рекомендуется к использованию в качестве ключа в Map, т.к. его параметр pathToFile, учитывающиеся в equals и hashCode, можно изменить
 * @author TheDiVaZo
 * @since 31.01.2025
 */
public final class ConfigWrapper<T> {
    private final ConfigLoader<T> configLoader;
    private Path pathToFile;
    private T actualConfig;
    private final T defaultConfig;
    private final Class<? extends T> configClass;

    public ConfigWrapper(Path pathToFile, ConfigLoader<T> configLoader, T defaultConfig) {
        if (defaultConfig == null || !ReflectionUtil.hasAnnotation(defaultConfig.getClass(), ConfigSerializable.class)) {
            throw new IllegalArgumentException("Config class has not have @ConfigSerializable annotation");
        }
        else if (!ReflectionUtil.hasEmptyConstructor(defaultConfig.getClass())) {
            throw new IllegalArgumentException("Config class has not have empty constructor");
        }
        this.pathToFile = pathToFile;
        this.configLoader = configLoader;
        this.configClass = (Class<? extends T>) defaultConfig.getClass();
        this.defaultConfig = defaultConfig;
        this.actualConfig = defaultConfig;
    }

    /**
     * Обновляет путь до файла конфигурации. Учьтите, что после обновления пути конфиг необходимо заново загрузить методом {@link #load()}
     * @param newPath
     */
    public void updatePath(Path newPath) {
        this.pathToFile = newPath;
    }

    public void load() {
        actualConfig = configLoader.load(pathToFile, defaultConfig);
    }

    public void save() {
        configLoader.save(pathToFile, actualConfig);
    }

    public T getConfig() {
        return actualConfig;
    }

    public Class<? extends T> getConfigClass() {
        return configClass;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ConfigWrapper<?> that = (ConfigWrapper<?>) o;
        return pathToFile.equals(that.pathToFile) && configClass.equals(that.configClass);
    }

    @Override
    public int hashCode() {
        int result = pathToFile.hashCode();
        result = 31 * result + configClass.hashCode();
        return result;
    }


}

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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;

/**
 * <p>Класс, инкапсулирующий в себе низкоуровневую работу с конкретным конфигом. Является представлением конфига
 * Данный класс не рекомендуется к использованию там, где работа кода зависит от реализации hashcode или equals,
 * т.к. его параметр pathToFile, учитывающиеся в equals и hashCode, можно изменить
 * </p>
 * @param <T> Класс конфигурации
 * @author TheDiVaZo
 * @since 31.01.2025
 */
public final class ConfigWrapper<T> {
    private final ConfigLoader configLoader;
    private volatile Path pathToFile;
    private volatile @Nullable T actualConfig;
    private final Class<? extends T> configClass;

    public ConfigWrapper(Path pathToFile, ConfigLoader configLoader, Class<? extends T> clazz) {
        if (!ReflectionUtil.hasAnnotation(clazz, ConfigSerializable.class)) {
            throw new IllegalArgumentException("Config class has not have @ConfigSerializable annotation");
        } else if (!ReflectionUtil.hasEmptyConstructor(clazz)) {
            throw new IllegalArgumentException("Config class has not have empty constructor");
        }
        this.pathToFile = pathToFile;
        this.configLoader = configLoader;
        this.configClass = clazz;
        this.actualConfig = null;
    }

    public static class Factory {
        private final ConfigLoader configLoader;

        public Factory(ConfigLoader configLoader) {
            this.configLoader = configLoader;
        }

        public <T> ConfigWrapper<T> create(Path pathToFile, Class<T> clazz) {
            return new ConfigWrapper<>(pathToFile, configLoader, clazz);
        }
    }

    /**
     * Обновляет путь до файла конфигурации. Учьтите, что после обновления пути конфиг необходимо заново загрузить методом {@link #load()}
     *
     * @param newPath новый путь
     */
    public synchronized void updatePath(Path newPath) {
        this.pathToFile = newPath;
    }

    public synchronized void load() {
        actualConfig = configLoader.load(pathToFile, configClass, true);
    }

    public synchronized void save() {
        configLoader.save(pathToFile, actualConfig);
    }

    public synchronized @Nullable T getConfig() {
        return actualConfig;
    }

    public synchronized @NonNull T getConfigOrLoad() {
        if (actualConfig == null) {
            load();
        }
        //Создаем вторую переменную, т.к. при использовании `actualConfig` Checker Framework выдает предупреждение
        T config = actualConfig;
        if (config == null) {
            throw new IllegalStateException("Invalid configuration loading");
        }
        return config;
    }

    public synchronized Class<? extends T> getConfigClass() {
        return configClass;
    }

    @Override
    public synchronized boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ConfigWrapper<?> that = (ConfigWrapper<?>) o;
        return pathToFile.equals(that.pathToFile) && configClass.equals(that.configClass);
    }

    @Override
    public synchronized int hashCode() {
        int result = pathToFile.hashCode();
        result = 31 * result + configClass.hashCode();
        return result;
    }
}

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

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс представляет собой контейнер конфигов. По классу конфигурации можно получить его представление через {@link ConfigWrapper}, если оно существует
 *
 * @author TheDiVaZo
 * @since 02.02.2025
 */
public class ConfigContainer {
    private final Map<Class<?>, ConfigWrapper<?>> container;

    /**
     * Создает контейнер конфигов из переданного множества представлений конфигураций
     *
     * @param configWrappers Множество представлений конфигураций
     */
    public ConfigContainer(Set<ConfigWrapper<?>> configWrappers) {
        this.container = configWrappers.stream().collect(Collectors.toMap(ConfigWrapper::getConfigClass, configWrapper -> configWrapper));
    }

    /**
     * Загружает все конфигурации, присутствующие в контейнере (вызывает метод {@link ConfigWrapper#load()} у всех представлений в контейнере)
     */
    public void loadAll() {
        for (ConfigWrapper<?> value : container.values()) {
            value.load();
        }
    }

    /**
     * Получает представление конфигурации по её классу конфигурации. Если класса конфигурации нет, вызывает исключение {@link IllegalArgumentException}
     *
     * @param clazz Класс конфигурации
     * @param <T>  Тип конфигурации
     * @return Представление конфигурации
     * @throws IllegalArgumentException если представления конфигурации нет
     */
    @SuppressWarnings("unchecked")
    public <T> ConfigWrapper<T> getConfig(Class<T> clazz) {
        Object config = container.get(clazz);
        if (config == null) {
            throw new IllegalArgumentException("No config for class: " + clazz);
        }
        return (ConfigWrapper<T>) config;
    }

    public Map<Class<?>, ConfigWrapper<?>> getContainer() {
        return container;
    }
}

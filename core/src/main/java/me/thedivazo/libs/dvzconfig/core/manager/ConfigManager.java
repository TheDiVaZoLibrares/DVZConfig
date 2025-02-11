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

package me.thedivazo.libs.dvzconfig.core.manager;

import me.thedivazo.libs.dvzconfig.core.config.ConfigContainer;
import me.thedivazo.libs.dvzconfig.core.config.ConfigWrapper;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Интерфейс, инкапсулирующий в себе работу с конфигурацией.
 *
 * @author TheDiVaZo
 * @since 03.02.2025
*/
public interface ConfigManager {
    /**
     * <p>В обычном коде данный метод вызываться не должен.
     * Он нужен для принудительного изменения каких то параметров конфигурации (путь до файла, принудительная перезагрузка, сохранение и т.п.)
     * Если вы просто хотите получить конфигурацию, вопспользуйтесь методом {@link #getConfig(Class)}
     * </p>
     * @param clazz класс конфигурации
     * @return предстиавление конфигурации
     * @param <T> класс конфигурации
     */
    <T> @NonNull ConfigWrapper<T> getWrapper(Class<T> clazz);

    /**
     * Получить конфигурацию по классу.
     * @param clazz класс конфигурации
     * @param <T> класс конфигурации
     * @return объект конфигурации
     */
    <T> @NonNull T getConfig(Class<T> clazz);

    ConfigContainer getConfigContainer();
    void load();
    void reload();
}

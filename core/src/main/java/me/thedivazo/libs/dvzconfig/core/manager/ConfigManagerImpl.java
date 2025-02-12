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
 * Реализация по умолчанию, делегирующая вызов метода загрузки и перезагрузки контейнеру {@link ConfigContainer}
 *
 * @author TheDiVaZo
 * @since 03.02.2025
 */
public class ConfigManagerImpl implements ConfigManager {
    protected final ConfigContainer container;

    public ConfigManagerImpl(ConfigContainer container) {
        this.container = container;
    }

    @Override
    public @NonNull <T> ConfigWrapper<T> getWrapper(Class<T> clazz) {
        return container.getWrapper(clazz);
    }

    @Override
    public @NonNull <T> T getConfig(Class<T> clazz) {
        return container.getConfig(clazz);
    }

    @Override
    public ConfigContainer getConfigContainer() {
        return container;
    }

    @Override
    public void load() {
        container.loadAll();
    }

    @Override
    public void reload() {
        container.loadAll();
    }
}

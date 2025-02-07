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
import java.util.function.Consumer;

/**
 * Реализация, предоставляющая возможность пользовательского метода перезагрузки конфига
 *
 * @author TheDiVaZo
 * @since 03.02.2025
 */
public class CustomReloadConfigManager implements ConfigManager {
    protected final ConfigContainer container;
    protected final Map<Class<?>, Consumer<? super ConfigWrapper<?>>> reloadFunctions;

    public CustomReloadConfigManager(ConfigContainer container, CustomConfigReloadClassMapBuilder reloadFuncContainer) {
        this.container = container;
        this.reloadFunctions = Map.copyOf(reloadFuncContainer.builder());
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
        for (Map.Entry<Class<?>, ConfigWrapper<?>> entry : container.getContainer().entrySet()) {
            Consumer<? super ConfigWrapper<?>> consumer = reloadFunctions.getOrDefault(entry.getKey(), ConfigWrapper::load);
            consumer.accept(entry.getValue());
        }
    }
}

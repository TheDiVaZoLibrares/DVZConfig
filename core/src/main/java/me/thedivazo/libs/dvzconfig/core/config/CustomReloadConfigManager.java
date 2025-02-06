package me.thedivazo.libs.dvzconfig.core.config;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
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

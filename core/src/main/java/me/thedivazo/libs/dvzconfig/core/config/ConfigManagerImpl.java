package me.thedivazo.libs.dvzconfig.core.config;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public class ConfigManagerImpl implements ConfigManager {
    protected final ConfigContainer container;

    public ConfigManagerImpl(ConfigContainer container) {
        this.container = container;
    }

    @Override
    public void load() {
        container.loadAll();
    }

    @Override
    public void reload() {
        for (ConfigWrapper<?> value : container.getContainer().values()) {
            value.reload();
        }
    }
}

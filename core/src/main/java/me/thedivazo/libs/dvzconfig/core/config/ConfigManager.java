package me.thedivazo.libs.dvzconfig.core.config;/**
* @author TheDiVaZo
* created on 03.02.2025
*/
public interface ConfigManager {
    ConfigContainer getConfigContainer();
    void load();
    void reload();
}

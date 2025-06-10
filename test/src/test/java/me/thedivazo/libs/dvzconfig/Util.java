package me.thedivazo.libs.dvzconfig;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedReader;
import java.io.StringReader;

public enum Util {;
    public static ConfigurationNode createNode(String yaml) {
        try {
            return YamlConfigurationLoader.builder().source(() -> new BufferedReader(new StringReader(yaml))).build().load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test configuration node", e);
        }
    }
}

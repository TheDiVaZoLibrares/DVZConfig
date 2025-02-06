package me.thedivazo.libs.dvzconfig.core.config;

import me.thedivazo.libs.dvzconfig.object.TestConfigOne;
import me.thedivazo.libs.dvzconfig.object.TestConfigTwo;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author TheDiVaZo
 * created on 06.02.2025
 */
public class ConfigWrapperTest {

    @Test
    void testNoEqualsAnyConfig() {
        ConfigWrapper<TestConfigOne> testConfigOneConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "one"), null, new TestConfigOne());
        ConfigWrapper<TestConfigTwo> testConfigTwoConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "two"), null, new TestConfigTwo());

        assertNotEquals(testConfigOneConfigWrapper, testConfigTwoConfigWrapper);
    }

    @Test
    void testEqualsSameConfig() {
        ConfigWrapper<TestConfigOne> testConfigOneConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "one"), null, new TestConfigOne());
        ConfigWrapper<TestConfigOne> testConfigTwoConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "one"), null, new TestConfigOne());

        assertEquals(testConfigOneConfigWrapper, testConfigTwoConfigWrapper);
    }

    @Test
    void testNotEqualsSamePathConfig() {
        ConfigWrapper<TestConfigOne> testConfigOneConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "one"), null, new TestConfigOne());
        ConfigWrapper<TestConfigTwo> testConfigTwoConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "one"), null, new TestConfigTwo());

        assertNotEquals(testConfigOneConfigWrapper, testConfigTwoConfigWrapper);
    }

    @Test
    void testNotEqualsSameTypeConfig() {
        ConfigWrapper<TestConfigOne> testConfigOneConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "one"), null, new TestConfigOne());
        ConfigWrapper<TestConfigOne> testConfigTwoConfigWrapper = new ConfigWrapper<>(Paths.get("path", "to", "config", "two"), null, new TestConfigOne());

        assertNotEquals(testConfigOneConfigWrapper, testConfigTwoConfigWrapper);
    }
}

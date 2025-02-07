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

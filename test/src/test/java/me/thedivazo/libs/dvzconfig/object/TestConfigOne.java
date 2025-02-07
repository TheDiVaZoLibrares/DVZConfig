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

package me.thedivazo.libs.dvzconfig.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;
import java.util.Set;

/**
 * @author TheDiVaZo
 * created on 06.02.2025
 */
@ConfigSerializable
@Setter
@Getter
@NoArgsConstructor
public class TestConfigOne {
    private String zooName = "moscow zoo";

    private Cow cow = new Cow(){{
        setAge(5);
        setName("Cow");
        setPregnant(true);
    }};

    private Map<String, Animal> map = Map.of(
            "papapa",
            new Cow() {{
                setPregnant(true);
                setAge(2);
                setName("Papapa");
            }},
            "pepepe",
            new Cat() {{
                setAge(5);
                setName("Pepepe");
                setColor("red");
            }});
}

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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * @since 05.02.2025
 */
@ConfigSerializable
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Cow extends Animal {
    {
        type = "cow";
    }

    boolean isPregnant;
    public Cow(int age, String name, boolean pregnant) {
        setAge(age);
        setName(name);
        this.isPregnant = pregnant;
    }
}

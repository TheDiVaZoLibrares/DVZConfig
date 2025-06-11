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
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheDiVaZo
 * @since 06.02.2025
 */
@ConfigSerializable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class TestConfigOne {
    private String zooName = "moscow zoo";

    private RewardItem rewardItem = new RewardItem() {{
        setItemsIdAndCounts(new HashMap<>(){{put("moscow", 1);put("zoo", 1);}});
    }};

    private RewardEffect rewardEffect = new RewardEffect(new ArrayList<>(){{add("SPEED");}}, 90d);

    private Cow cow = new Cow(){{
        setAge(5);
        setName("Cow");
        setPregnant(true);
    }};

    @Setting("map")
    private Map<String, Animal> map = new HashMap<>() {{
        put("papapa", new Cow(2, "Papapa", true));
        put("pepepe", new Cat(5, "Pepepe", "red"));
    }};
}

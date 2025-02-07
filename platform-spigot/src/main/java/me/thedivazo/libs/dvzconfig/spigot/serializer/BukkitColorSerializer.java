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

package me.thedivazo.libs.dvzconfig.spigot.serializer;

import io.leangen.geantyref.TypeToken;
import me.thedivazo.libs.dvzconfig.core.serializer.ColorSerializer;
import org.bukkit.Color;

import java.util.Map;

public class BukkitColorSerializer extends ColorSerializer<Color> {
    public static final BukkitColorSerializer DEFAULT;
    static {
        DEFAULT = new BukkitColorSerializer(Map.ofEntries(
                Map.entry("black", Color.BLACK.asARGB()),
                Map.entry("aqua", Color.AQUA.asARGB()),
                Map.entry("blue", Color.BLUE.asARGB()),
                Map.entry("fuchsia", Color.FUCHSIA.asARGB()),
                Map.entry("gray", Color.GRAY.asARGB()),
                Map.entry("green", Color.GREEN.asARGB()),
                Map.entry("lime", Color.LIME.asARGB()),
                Map.entry("maroon", Color.MAROON.asARGB()),
                Map.entry("navy", Color.NAVY.asARGB()),
                Map.entry("olive", Color.OLIVE.asARGB()),
                Map.entry("yellow", Color.YELLOW.asARGB()),
                Map.entry("white", Color.WHITE.asARGB()),
                Map.entry("teal", Color.TEAL.asARGB()),
                Map.entry("silver", Color.SILVER.asARGB()),
                Map.entry("red", Color.RED.asARGB()),
                Map.entry("purple", Color.PURPLE.asARGB()),
                Map.entry("orange", Color.ORANGE.asARGB())
        ));
    }

    protected BukkitColorSerializer(Map<String, Integer> colorNamesModel) {
        super(TypeToken.get(Color.class), colorNamesModel);
    }

    @Override
    protected Color fromARGB(int argb) {
        return Color.fromARGB(argb);
    }

    @Override
    protected int toARGB(Color color) {
        return color.asARGB();
    }
}

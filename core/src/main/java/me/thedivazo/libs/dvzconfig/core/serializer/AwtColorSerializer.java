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

package me.thedivazo.libs.dvzconfig.core.serializer;

import io.leangen.geantyref.TypeToken;

import java.awt.*;
import java.util.Map;

public class AwtColorSerializer extends ColorSerializer<Color>{
    public static final AwtColorSerializer DEFAULT = new AwtColorSerializer(Map.ofEntries(
            Map.entry("black", Color.BLACK.getRGB()),
            Map.entry("white", Color.WHITE.getRGB()),
            Map.entry("red", Color.RED.getRGB()),
            Map.entry("green", Color.GREEN.getRGB()),
            Map.entry("blue", Color.BLUE.getRGB()),
            Map.entry("yellow", Color.YELLOW.getRGB()),
            Map.entry("cyan", Color.CYAN.getRGB()),
            Map.entry("magenta", Color.MAGENTA.getRGB()),
            Map.entry("gray", Color.GRAY.getRGB()),
            Map.entry("lightGray", Color.LIGHT_GRAY.getRGB()),
            Map.entry("darkGray", Color.DARK_GRAY.getRGB()),
            Map.entry("orange", Color.ORANGE.getRGB()),
            Map.entry("pink", Color.PINK.getRGB())
    ));

    public AwtColorSerializer(Map<String, Integer> colorNamesModel) {
        super(TypeToken.get(Color.class), colorNamesModel);
    }

    @Override
    protected Color fromARGB(int argb) {
        return new Color(argb, true);
    }

    @Override
    protected int toARGB(Color color) {
        return color.getRGB();
    }
}

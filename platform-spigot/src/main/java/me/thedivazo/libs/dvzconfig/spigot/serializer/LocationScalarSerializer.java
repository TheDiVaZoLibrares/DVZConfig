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

import me.thedivazo.libs.dvzconfig.core.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.index.qual.NonNegative;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static me.thedivazo.libs.dvzconfig.core.util.RegexUtil.POINT_NUM_PATTERN;
import static me.thedivazo.libs.dvzconfig.core.util.RegexUtil.WORLD_NAME_PATTERN;

/**
 * @author TheDiVaZo
 * created on 05.02.2025
 */
public class LocationScalarSerializer extends ScalarSerializer<Location> {
    public static final LocationScalarSerializer DEFAULT = new LocationScalarSerializer(WORLD_NAME_PATTERN, POINT_NUM_PATTERN);
    private static final DecimalFormat DECIMAL_FORMAT;

    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');

        DECIMAL_FORMAT = new DecimalFormat("0.#######", otherSymbols);
    }

    //Эта хрень должна согласовываться с LOCATION_PATTERN_STR
    private static final String WORLD_FORMAT_CHUNK = "%s:";
    private static final String COORDINATES_FORMAT_CHUNK = "%s:%s:%s";
    private static final String ROTATION_COORD_FORMAT_CHUNK = "|%s:%s";

    //Не забыть: При изменении LOCATION_PATTERN_STR нужно так-же менять все номера групп в соответствии с номером в LOCATION_PATTERN_STR
    //Пример нумерации групп: 1()1 2(3()3 4(5()5 6()6)4)2 7()7 8()8 - цифры - обозначение номера группы
    private static final String LOCATION_PATTERN_STR = "^((%s):)?(%s):(%s):(%s)(\\|(%s):(%s))?$";
    private static final int NUMBER_GROUP_WORLD_NAME = 2;
    private static final int NUMBER_GROUP_X = 3;
    private static final int NUMBER_GROUP_Y = 4;
    private static final int NUMBER_GROUP_Z = 5;
    private static final int NUMBER_GROUP_YAW = 7;
    private static final int NUMBER_GROUP_PITCH = 8;

    private final Pattern locationPattern;
    private final @NonNegative int worldNameGroupShift;
    private final @NonNegative int coordinateGroupShift;

    protected LocationScalarSerializer(String worldNamePatternStr, String coordinatePatternStr) {
        super(Location.class);

        try {
            Pattern.compile(worldNamePatternStr);
            Pattern.compile(coordinatePatternStr);
        } catch (PatternSyntaxException exception) {
            throw new IllegalArgumentException("Invalid world name ("+worldNamePatternStr+") and coordinate ("+coordinatePatternStr+") patterns", exception);
        }

        locationPattern = Pattern.compile(String.format(LOCATION_PATTERN_STR, worldNamePatternStr, coordinatePatternStr, coordinatePatternStr, coordinatePatternStr, coordinatePatternStr, coordinatePatternStr));
        worldNameGroupShift = StringUtil.count(worldNamePatternStr, '(');
        coordinateGroupShift = StringUtil.count(coordinatePatternStr, '(');
    }

    @Override
    public Location deserialize(Type type, Object obj) throws SerializationException {
        String strValue = obj.toString();
        Matcher matcher = locationPattern.matcher(strValue);
        if (!matcher.find()) {
            throw new SerializationException("Invalid location string: \"" + strValue + "\", regEx format: \"" + locationPattern +"\"");
        }
        String worldName = matcher.group(NUMBER_GROUP_WORLD_NAME);

        try {
            String matchedX = matcher.group(NUMBER_GROUP_X + worldNameGroupShift);
            if (matchedX == null) {
                throw new SerializationException("Missing X coordinate in location string: \"" + strValue + "\"");
            }
            double x = Double.parseDouble(matchedX);

            String matchedY = matcher.group(NUMBER_GROUP_Y + worldNameGroupShift + coordinateGroupShift);
            if (matchedY == null) {
                throw new SerializationException("Missing Y coordinate in location string: \"" + strValue + "\"");
            }
            double y = Double.parseDouble(matchedY);

            String matchedZ = matcher.group(NUMBER_GROUP_Z + worldNameGroupShift + coordinateGroupShift * 2);
            if (matchedZ == null) {
                throw new SerializationException("Missing Z coordinate in location string: \"" + strValue + "\"");
            }
            double z = Double.parseDouble(matchedZ);

            String strYaw = matcher.group(NUMBER_GROUP_YAW + worldNameGroupShift + coordinateGroupShift * 3);
            String strPitch = matcher.group(NUMBER_GROUP_PITCH + worldNameGroupShift + coordinateGroupShift * 4);
            float yaw = 0;
            float pitch = 0;
            if (strYaw != null && !strYaw.isEmpty()) {
                yaw = Float.parseFloat(strYaw);
            }
            if (strPitch != null && !strPitch.isEmpty()) {
                pitch = Float.parseFloat(strPitch);
            }

            return new Location(worldName == null ? null : Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        } catch (NumberFormatException exception) {
            throw new SerializationException(exception);
        }
    }

    @Override
    protected Object serialize(Location item, Predicate<Class<?>> typeSupported) {
        StringBuilder stringBuilder = new StringBuilder();
        World world = item.getWorld();
        if (world != null) {
            stringBuilder.append(String.format(WORLD_FORMAT_CHUNK, world.getName()));
        }

        stringBuilder.append(String.format(COORDINATES_FORMAT_CHUNK,
                DECIMAL_FORMAT.format(item.getX()),
                DECIMAL_FORMAT.format(item.getY()),
                DECIMAL_FORMAT.format(item.getZ())));

        if (item.getYaw()!= 0 || item.getPitch()!= 0) {
            stringBuilder.append(String.format(ROTATION_COORD_FORMAT_CHUNK, DECIMAL_FORMAT.format(item.getYaw()), DECIMAL_FORMAT.format(item.getPitch())));
        }

        return stringBuilder.toString();
    }
}

package me.thedivazo.libs.dvzconfig.spigot.serializer;

import me.thedivazo.libs.dvzconfig.core.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
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

    //Эта хрень должна согласовываться с LOCATION_PATTERN_STR
    private static final String WORLD_FORMAT_CHUNK = "%s:";
    private static final String COORDINATE_FORMAT_CHUNK = "%f:%f:%f";
    private static final String ROTATION_COORD_FORMAT_CHUNK = "|%f:%f";

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
    private final int worldNameGroupShift;
    private final int coordinateGroupShift;

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
            throw new SerializationException("Invalid location string: " + strValue + ", regEx format: " + locationPattern);
        }
        String worldName = matcher.group(NUMBER_GROUP_WORLD_NAME);
        double x = Double.parseDouble(matcher.group(NUMBER_GROUP_X + worldNameGroupShift));
        double y = Double.parseDouble(matcher.group(NUMBER_GROUP_Y + worldNameGroupShift + coordinateGroupShift));
        double z = Double.parseDouble(matcher.group(NUMBER_GROUP_Z + worldNameGroupShift + coordinateGroupShift*2));
        String strYaw = matcher.group(NUMBER_GROUP_YAW + worldNameGroupShift + coordinateGroupShift*3);
        String strPitch = matcher.group(NUMBER_GROUP_PITCH + worldNameGroupShift + coordinateGroupShift*4);

        float yaw = 0;
        float pitch = 0;

        if (strYaw != null && !strYaw.isEmpty()) {
            yaw = Float.parseFloat(strYaw);
        }
        if (strPitch != null && !strPitch.isEmpty()) {
            pitch = Float.parseFloat(strPitch);
        }

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    @Override
    protected Object serialize(Location item, Predicate<Class<?>> typeSupported) {
        StringBuilder stringBuilder = new StringBuilder();
        if (item.getWorld() != null) {
            stringBuilder.append(String.format(WORLD_FORMAT_CHUNK, item.getWorld().getName()));
        }

        stringBuilder.append(String.format(COORDINATE_FORMAT_CHUNK, item.getX(), item.getY(), item.getZ()));

        if (item.getYaw()!= 0 || item.getPitch()!= 0) {
            stringBuilder.append(String.format(ROTATION_COORD_FORMAT_CHUNK, item.getYaw(), item.getPitch()));
        }

        return stringBuilder.toString();
    }
}

package me.thedivazo.libs.dvzconfig.core.util;

/**
 * @author TheDiVaZo
 * created on 05.02.2025
 */
public final class StringUtil {
    public static int count(String str, char symbol) {
        return (int) str.chars().filter(ch -> ch == symbol).count();
    }
}

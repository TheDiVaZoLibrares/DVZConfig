package me.thedivazo.libs.dvzconfig.core.util;

import org.checkerframework.checker.index.qual.NonNegative;

/**
 * @author TheDiVaZo
 * created on 05.02.2025
 */
public final class StringUtil {
    public static @NonNegative int count(String str, char symbol) {
        return CheckerFrameworkUtil.safeLongToInt(str.chars().filter(ch -> ch == symbol).count());
    }
}

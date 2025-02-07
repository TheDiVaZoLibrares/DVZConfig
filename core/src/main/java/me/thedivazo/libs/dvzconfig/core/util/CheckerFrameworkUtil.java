package me.thedivazo.libs.dvzconfig.core.util;

import org.checkerframework.checker.index.qual.NonNegative;

/**
 * @author TheDiVaZo
 * created on 07.02.2025
 */
public final class CheckerFrameworkUtil {
    public static @NonNegative int safeLongToInt(@NonNegative long value) {
        if (value > Integer.MAX_VALUE) {
            throw new ArithmeticException("Value too large for int: " + value);
        }
        return (@NonNegative int) value;
    }
}

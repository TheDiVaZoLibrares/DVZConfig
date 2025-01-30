package me.thedivazo.library.dvzconfig.core.util;

import java.util.Arrays;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public final class ReflectionUtil {
    public static boolean hasAnnotation(Class<?> clazz, Class<?> annotationClazz) {
        return Arrays.stream(clazz.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType() == annotationClazz);
    }

    public static boolean hasEmptyConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
    }
}

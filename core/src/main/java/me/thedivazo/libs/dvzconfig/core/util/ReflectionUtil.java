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

package me.thedivazo.libs.dvzconfig.core.util;

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

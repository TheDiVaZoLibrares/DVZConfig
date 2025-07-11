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

import lombok.SneakyThrows;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface FunctionChecked<T, R, E extends Exception> extends Function<T, R> {
    @SneakyThrows
    @Override
    default R apply(T t) {
        return applyOrException(t);
    }

    R applyOrException(T t) throws E;

    default <V> FunctionChecked<V, R, E> compose(FunctionChecked<? super V, ? extends T, ? extends E> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> FunctionChecked<T, V, E> andThen(FunctionChecked<? super R, ? extends V, ? extends E> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T, E extends Exception> FunctionChecked<T, T, E> identity() {
        return t -> t;
    }
}
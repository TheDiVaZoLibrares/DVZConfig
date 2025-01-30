package me.thedivazo.library.dvzconfig.core;

import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 31.01.2025
 */
public interface ClassContainer<T> {
    <O extends T> O get(Class<O> clazz);

    <O extends T> boolean contains(Class<O> clazz);

    Map<Class<? extends T>, T> getContainer();
}

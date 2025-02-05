package me.thedivazo.libs.dvzconfig.core.config;

import me.thedivazo.libs.dvzconfig.core.util.ClassMapBuilder;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public class CustomConfigReloadClassMapBuilder extends ClassMapBuilder<Consumer<? super ConfigWrapper<?>>> {
    protected CustomConfigReloadClassMapBuilder(Supplier<Map<Class<?>, Consumer<? super ConfigWrapper<?>>>> supplier) {
        super(supplier);
    }

    @SuppressWarnings("unchecked")
    public <T> CustomConfigReloadClassMapBuilder put(Class<T> clazz, Consumer<? super ConfigWrapper<T>> reloadConsumer) {
        container.put(clazz, (Consumer<? super ConfigWrapper<?>>) reloadConsumer);
        return this;
    }
}

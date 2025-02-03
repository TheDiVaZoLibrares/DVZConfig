package me.thedivazo.libs.dvzconfig.core.container;

import me.thedivazo.libs.dvzconfig.core.config.ConfigWrapper;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author TheDiVaZo
 * created on 03.02.2025
 */
public class ConfReloadFuncMutableContainer extends TypeSafeContainer<Consumer<? super ConfigWrapper<?>>>{
    protected ConfReloadFuncMutableContainer(Supplier<Map<Class<?>, Consumer<? super ConfigWrapper<?>>>> supplier) {
        super(supplier);
    }

    @SuppressWarnings("unchecked")
    public <T> void put(Class<T> clazz, Consumer<? super ConfigWrapper<T>> reloadConsumer) {
        container.put(clazz, (Consumer<? super ConfigWrapper<?>>) reloadConsumer);
    }
}

package io.github.ruby.module.setting;

import io.github.ruby.module.AbstractModule;
import io.github.ruby.storage.impl.ModuleStorage;

import java.util.function.Consumer;

public abstract class AbstractSetting<T> {
    public final String name, description;
    private T value;

    public Consumer<T> changeListener = null;

    public AbstractSetting(String name, String description, T value) {
        this.name = name;
        this.description = description;
        this.value = value;

        AbstractModule parent = ModuleStorage.INSTANCE.lastModule;
        parent.settings.add(this);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        if(changeListener != null) changeListener.accept(value);
    }
}
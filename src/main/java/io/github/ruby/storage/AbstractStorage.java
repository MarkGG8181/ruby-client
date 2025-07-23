package io.github.ruby.storage;

import io.github.ruby.util.IUtility;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorage<T> implements IUtility, IStorage<T> {
    public final List<T> list = new ArrayList<>();

    public void add(T thing) {
        list.add(thing);
    }

    public <U extends T> U getByClass(Class<U> clazz) {
        for (T thing : list) {
            if (clazz.isInstance(thing)) {
                return clazz.cast(thing);
            }
        }
        return null;
    }
}
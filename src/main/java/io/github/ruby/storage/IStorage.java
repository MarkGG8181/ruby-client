package io.github.ruby.storage;

public interface IStorage<T> {
    void init();
    void add(T t);
}
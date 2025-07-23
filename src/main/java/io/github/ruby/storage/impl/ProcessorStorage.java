package io.github.ruby.storage.impl;

import io.github.ruby.processor.AbstractProcessor;
import io.github.ruby.processor.impl.ScreenProcessor;
import io.github.ruby.storage.AbstractStorage;

public class ProcessorStorage extends AbstractStorage<AbstractProcessor> {
    public static final ProcessorStorage INSTANCE = new ProcessorStorage();

    @Override
    public void init() {
        add(new ScreenProcessor());
        list.forEach(AbstractProcessor::init);
    }
}
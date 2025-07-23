package io.github.ruby.processor;

import io.github.ruby.util.IUtility;

public abstract class AbstractProcessor implements IUtility, IProcessor {
    @Override
    public void init() {
        BUS.register(this);
    }
}
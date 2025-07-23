package io.github.ruby.event.impl;

import io.github.ruby.event.AbstractEvent;

public class KeyboardInputEvent extends AbstractEvent {
    public final int key;

    public KeyboardInputEvent(int key) {
        this.key = key;
    }
}

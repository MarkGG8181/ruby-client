package io.github.ruby.event;

import io.github.ruby.Ruby;

public abstract class AbstractEvent {
    public EventState state = EventState.PRE;

    public boolean isPre() {
        return state.equals(EventState.PRE);
    }

    public void post() {
        Ruby.BUS.post(this);
    }
}
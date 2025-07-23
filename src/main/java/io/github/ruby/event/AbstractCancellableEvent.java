package io.github.ruby.event;

public abstract class AbstractCancellableEvent extends AbstractEvent {
    public boolean cancelled = false;
}
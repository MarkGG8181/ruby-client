package io.github.ruby.event.impl;

import io.github.ruby.event.AbstractEvent;

public class ResizeWindowEvent extends AbstractEvent {
    public final float newWidth, newHeight;
    public final float adjustedWidth, adjustedHeight;

    public ResizeWindowEvent(float newWidth, float newHeight, float adjustedWidth, float adjustedHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.adjustedWidth = adjustedWidth;
        this.adjustedHeight = adjustedHeight;
    }
}
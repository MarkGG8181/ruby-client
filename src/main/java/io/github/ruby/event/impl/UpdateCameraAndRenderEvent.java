package io.github.ruby.event.impl;

import io.github.ruby.event.AbstractEvent;

public class UpdateCameraAndRenderEvent extends AbstractEvent {
    public final float partialTicks;
    public final int mouseX, mouseY;
    public final float screenWidth, screenHeight;

    public UpdateCameraAndRenderEvent(float partialTicks, int mouseX, int mouseY, float screenWidth, float screenHeight) {
        this.partialTicks = partialTicks;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
}

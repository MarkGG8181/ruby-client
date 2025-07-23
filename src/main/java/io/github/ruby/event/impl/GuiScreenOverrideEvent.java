package io.github.ruby.event.impl;

import io.github.ruby.event.AbstractEvent;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenOverrideEvent extends AbstractEvent {
    public GuiScreen screen;

    public GuiScreenOverrideEvent(GuiScreen screen) {
        this.screen = screen;
    }
}

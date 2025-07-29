package io.github.ruby.screen;

import io.github.ruby.screen.element.AbstractElement;
import io.github.ruby.util.game.shader.ShaderUtil;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScreen extends GuiScreen implements IFonts, IColors {
    public final String name;

    public final List<AbstractElement> children = new ArrayList<>();

    public AbstractScreen(String name) {
        this.name = name;
    }

    @Override
    public void onInit() {
        children.clear();
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        ShaderUtil.rect(0, 0, width, height, BACKGROUND);
        children.forEach(c -> c.onRender(mouseX, mouseY, partialTicks));
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        children.forEach(c -> c.onClick(mouseX, mouseY, mouseButton));
    }
}
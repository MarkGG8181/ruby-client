package io.github.ruby.screen.impl;

import io.github.ruby.module.ModuleCategory;
import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.element.impl.TextElement;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class ClickGuiScreen extends AbstractScreen {
    private final HashMap<ModuleCategory, Vector2f> positions = new HashMap<>();

    public ClickGuiScreen() {
        super("Click Gui");

        float offset = 5;
        for (ModuleCategory value : ModuleCategory.values()) {
            positions.put(value, new Vector2f(offset, 5));
            offset += 80 + 4;
        }
    }

    @Override
    public void onInit() {
        super.onInit();
        for (Map.Entry<ModuleCategory, Vector2f> entry : positions.entrySet()) {
            children.add(TextElement.builder()
                    .text(entry.getKey().toString())
                    .position(entry.getValue().getX(), entry.getValue().getY())
                    .background(true)
                    .build(this)
            );
        }
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        children.forEach(c -> c.onRender(mouseX, mouseY, partialTicks));
    }
}
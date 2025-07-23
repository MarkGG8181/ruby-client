package io.github.ruby.screen.impl;

import io.github.ruby.module.AbstractModule;
import io.github.ruby.module.ModuleCategory;
import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.element.impl.ButtonElement;
import io.github.ruby.screen.element.impl.TextElement;
import io.github.ruby.screen.element.option.TextAlignment;
import io.github.ruby.storage.impl.ModuleStorage;
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
                    .textAlignment(TextAlignment.CENTER)
                    .background(true)
                    .build(this)
            );

            float offset = entry.getValue().getY() + 13.5f;
            for (AbstractModule module : ModuleStorage.INSTANCE.getByCategory(entry.getKey())) {
                children.add(ButtonElement.builder()
                        .text(module.getName())
                        .textAlignment(TextAlignment.CENTER_LEFT)
                        .position(entry.getValue().getX(), offset)
                        .onClick(() -> module.setEnabled(!module.isEnabled()))
                        .build(this)
                );
                offset += 14;
            }
        }
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        children.forEach(c -> c.onRender(mouseX, mouseY, partialTicks));
    }
}
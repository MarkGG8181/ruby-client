package io.github.ruby.screen.impl;

import io.github.ruby.module.ModuleCategory;
import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.util.game.shader.ShaderUtil;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class ClickGuiScreen extends AbstractScreen {
    private final HashMap<ModuleCategory, Vector2f> positions = new HashMap<>();

    private static final Vector2f size = new Vector2f(100, 16);

    public ClickGuiScreen() {
        super("Click Gui");
    }

    @Override
    public void onInit() {
        super.onInit();

        positions.clear();

        var offset = 5f;
        for (ModuleCategory category : ModuleCategory.values()) {
            positions.put(category, new Vector2f(offset, 5));
            offset += size.x + 5;
        }
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        for (Map.Entry<ModuleCategory, Vector2f> entry : positions.entrySet()) {
            ModuleCategory category = entry.getKey();
            Vector2f position = entry.getValue();

            ShaderUtil.rect(position.x, position.y, size.x, size.y, 5, BACKGROUND);
        }
    }
}
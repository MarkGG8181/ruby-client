package io.github.ruby.screen.impl;

import io.github.ruby.module.AbstractModule;
import io.github.ruby.module.ModuleCategory;
import io.github.ruby.module.setting.AbstractSetting;
import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.storage.impl.ModuleStorage;
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

            ShaderUtil.hGrad(position.x, position.y, size.x, size.y, 0, RUBY, PINK);
            ICON_22.drawString(category.icon.character, position.x + 3, position.y + 4, -1);
            MEDIUM_18.drawString(category.toString(), position.x + 3 + 15, position.y + 4.5f, -1);

            MEDIUM_20.drawString(category.expanded ? "_" : "+", position.x + size.x - 3 - 8, category.expanded ? position.y + .5f : position.y + 3.5f, -1);

            if (category.expanded) {
                var offset = position.y + size.y - .7f;
                for (AbstractModule module : ModuleStorage.INSTANCE.getByCategory(category)) {
                    ShaderUtil.rect(position.x, offset, size.x, size.y, PANEL);
                    REGULAR_18.drawString(module.getName(), position.x + 3, offset + 4.5f, module.isEnabled() ? RUBY.getRGB() : -1);

                    if (!module.settings.isEmpty()) {
                        MEDIUM_20.drawString(module.isExpanded() ? "_" : "+", position.x + size.x - 3 - 8, module.isExpanded() ? offset + .5f : offset + 3.5f, -1);

                        if (module.isExpanded()) {
                            offset += size.y - .7f;
                            for (AbstractSetting<?> setting : module.settings) {
                                ShaderUtil.rect(position.x, offset, size.x, size.y, PANEL);
                                REGULAR_16.drawString(setting.name, position.x + 3, offset + 5, -1);
                                offset += size.y;
                            }
                        }
                    }

                    offset += size.y - .55f;
                }
            }
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        for (Map.Entry<ModuleCategory, Vector2f> entry : positions.entrySet()) {
            ModuleCategory category = entry.getKey();
            Vector2f position = entry.getValue();

            if (hovered(mouseX, mouseY, position) && mouseButton == 1) {
                category.expanded = !category.expanded;
            }

            if (category.expanded) {
                var offset = position.y + size.y - .55f;
                for (AbstractModule module : ModuleStorage.INSTANCE.getByCategory(category)) {
                    if (hovered(mouseX, mouseY, new Vector2f(position.x, offset))) {
                        switch (mouseButton) {
                            case 0 -> module.setEnabled(!module.isEnabled());
                            case 1 -> module.setExpanded(!module.isExpanded());
                        }
                    }

                    offset += size.y - .55f;
                }
            }
        }
    }

    private boolean hovered (int mouseX, int mouseY, Vector2f position) {
        return mouseX >= position.x && mouseY >= position.y && mouseX < position.x + size.x && mouseY < position.y + size.y;
    }
}
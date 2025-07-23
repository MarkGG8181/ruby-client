package io.github.ruby.screen.element;

import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.IColors;
import io.github.ruby.screen.IFonts;
import io.github.ruby.screen.element.option.TextAlignment;
import io.github.ruby.screen.element.option.TextScale;
import io.github.ruby.util.IUtility;
import io.github.ruby.util.game.font.impl.CustomFontRenderer;

public abstract class AbstractElement implements IFonts, IColors, IUtility {
    public final AbstractScreen parent;
    public final String name;

    public final float x, y;
    public final float width, height;

    public Runnable runnable = null;

    public TextScale textScale = TextScale.NORMAL;
    public TextAlignment textAlignment = TextAlignment.TOP_LEFT;
    public boolean medium = false, background = true;

    public AbstractElement(String name, float x, float y, float width, float height, AbstractScreen parent) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public void onRender(int mouseX, int mouseY, float partialTicks) {}
    public void onClick(int mouseX, int mouseY, int mouseButton) {}
    public void onType(char character, int key) {}

    public CustomFontRenderer getFont() {
        return switch (textScale) {
            case SMALL -> medium ? MEDIUM_16 : REGULAR_16;
            case LARGE -> medium ? MEDIUM_20 : REGULAR_20;
            default -> medium ? MEDIUM_18 : REGULAR_18;
        };
    }

    public boolean hovered(float mouseX, float mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
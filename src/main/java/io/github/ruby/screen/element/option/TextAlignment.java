package io.github.ruby.screen.element.option;

import io.github.ruby.util.game.font.IFontRenderer;
import org.lwjgl.util.vector.Vector2f;

public enum TextAlignment {
    TOP_LEFT, BOTTOM_LEFT, CENTER_LEFT, CENTER, CENTER_RIGHT, TOP_RIGHT, BOTTOM_RIGHT;

    public Vector2f getPos(IFontRenderer font, String text, float width, float height) {
        float textWidth = font.getWidth(text);
        float textHeight = font.getHeight();

        return switch (this) {
            case CENTER -> new Vector2f((width - textWidth) / 2f - 2, (height - textHeight) / 2f + 0.5f);
            case CENTER_LEFT -> new Vector2f(0, (height - textHeight) / 2f);
            case CENTER_RIGHT -> new Vector2f(width - textWidth, (height - textHeight) / 2f);
            case TOP_RIGHT -> new Vector2f(width - textWidth, 0);
            case BOTTOM_RIGHT -> new Vector2f(width - textWidth, height - textHeight);
            case BOTTOM_LEFT -> new Vector2f(0, height - textHeight);
            default -> new Vector2f(0, 0); // TOP_LEFT
        };
    }
}
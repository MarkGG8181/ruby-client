package io.github.ruby.util.game.font;

public interface IFontRenderer {
    int drawStringWithShadow(String text, float x, float y, int color);

    int drawString(String text, float x, float y, int color);
    int drawString(String text, float x, float y, int color, boolean shadow);

    int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow);
    int renderString(String text, float x, float y, int color, boolean dropShadow);

    int getWidth(String text);
    int getHeight();
}
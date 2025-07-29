package io.github.ruby.screen;

import java.awt.*;

public interface IColors {
    Color RUBY = new Color(0xEB0000);
    Color MAGENTA = new Color(0xF55376);
    Color PINK = new Color(0xFA91AD);
    Color BACKGROUND = new Color(20, 20, 20);
    Color FRAME = new Color(25, 25, 25);
    Color PANEL = new Color(30, 30, 30);
    Color TEXT = new Color(255, 255, 255);

    default Color withAlpha(Color color, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha)); // clamp alpha between 0 and 255
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    default Color withAlpha(Color color, float alpha) {
        alpha = Math.max(0f, Math.min(1f, alpha)); // clamp alpha between 0.0 and 1.0
        return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);
    }
}
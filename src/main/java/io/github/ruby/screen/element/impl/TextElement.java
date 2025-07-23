package io.github.ruby.screen.element.impl;

import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.element.AbstractElement;
import io.github.ruby.screen.element.option.TextAlignment;
import io.github.ruby.screen.element.option.TextScale;
import io.github.ruby.util.game.shader.ShaderUtil;
import org.lwjgl.util.vector.Vector2f;

public class TextElement extends AbstractElement {
    private final String text;
    private final float textX, textY;

    private TextElement(Builder builder) {
        super(builder.name != null ? builder.name : builder.text, builder.x, builder.y, builder.width, builder.height, builder.parent);

        this.text = builder.text;
        this.textScale = builder.textScale;
        this.medium = builder.medium;
        this.runnable = builder.runnable;
        this.textAlignment = builder.textAlignment;
        this.background = builder.background;

        Vector2f textPos = textAlignment.getPos(getFont(), text, width, height);

        this.textX = x + textPos.getX();
        this.textY = y + textPos.getY();
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        if(background)
            ShaderUtil.rect(x, y, width, height, hovered(mouseX, mouseY) ? PANEL.brighter() : PANEL);
        getFont().drawString(text, textX, textY, TEXT.getRGB());
    }


    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        if (runnable == null) return;

        if (hovered(mouseX, mouseY)) {
            runnable.run();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String text;
        private float x, y;
        private float width = 80, height = 14;
        private AbstractScreen parent;
        private TextScale textScale = TextScale.NORMAL;
        private boolean medium = false;
        private boolean background = true;
        private Runnable runnable = null;
        private TextAlignment textAlignment = TextAlignment.TOP_LEFT;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder position(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder textScale(TextScale size) {
            this.textScale = size;
            return this;
        }

        public Builder medium(boolean medium) {
            this.medium = medium;
            return this;
        }

        public Builder background(boolean background) {
            this.background = background;
            return this;
        }

        public Builder onClick(Runnable runnable) {
            this.runnable = runnable;
            return this;
        }

        public Builder textAlignment(TextAlignment textAlignment) {
            this.textAlignment = textAlignment;
            return this;
        }

        public TextElement build(AbstractScreen parent) {
            this.parent = parent;

            if (text == null) {
                text = name;
                LOGGER.warn("Please make sure text isn't null");
            }

            if (parent == null) {
                throw new IllegalStateException("Element parent must not be null!");
            }

            return new TextElement(this);
        }
    }
}
package io.github.ruby.screen.element.impl;

import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.element.AbstractElement;
import io.github.ruby.screen.element.option.TextAlignment;
import io.github.ruby.screen.element.option.TextScale;
import io.github.ruby.util.game.shader.ShaderUtil;
import org.lwjgl.util.vector.Vector2f;

public class ImageElement extends AbstractElement {
    private final String texture;
    private final float radius;

    private ImageElement(Builder builder) {
        super(builder.name, builder.x, builder.y, builder.width, builder.height, builder.parent);

        this.textScale = builder.textScale;
        this.runnable = builder.runnable;
        this.background = builder.background;
        this.texture = builder.texture;
        this.radius = builder.radius;
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        if (background)
            ShaderUtil.rect(x, y, width, height, hovered(mouseX, mouseY) ? PANEL.brighter() : PANEL);

        ShaderUtil.image(texture, x, y, width, height, radius);
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
        private String texture;
        private float x, y;
        private float width = 120, height = 14;
        private AbstractScreen parent;
        private TextScale textScale = TextScale.NORMAL;
        private boolean background = true;
        private Runnable runnable = null;
        private float radius = 0;

        public Builder texture(String texture) {
            this.name = texture;
            this.texture = texture;
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

        public Builder background(boolean background) {
            this.background = background;
            return this;
        }

        public Builder onClick(Runnable runnable) {
            this.runnable = runnable;
            return this;
        }

        public Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        public ImageElement build(AbstractScreen parent) {
            this.parent = parent;

            if (parent == null) {
                throw new IllegalStateException("Element parent must not be null!");
            }

            return new ImageElement(this);
        }
    }
}
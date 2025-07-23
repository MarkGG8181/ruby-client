package io.github.ruby.screen.element.impl;

import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.element.AbstractElement;
import io.github.ruby.screen.element.option.TextAlignment;
import io.github.ruby.screen.element.option.TextScale;
import io.github.ruby.util.game.shader.ShaderUtil;
import org.lwjgl.util.vector.Vector2f;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListElement extends AbstractElement {
    private final String[] content;
    private final float offsetAmount;
    private final TextScale contentScale;

    private final List<TextElement> entries = new ArrayList<>();

    private ListElement(Builder builder) {
        super(builder.title, builder.x, builder.y, builder.width, builder.height, builder.parent);

        this.content = builder.content;
        this.offsetAmount = builder.offsetAmount;
        this.contentScale = builder.contentScale;
        this.background = builder.background;
        this.medium = builder.medium;
        this.textAlignment = builder.textAlignment;

        float offsetY = y;
        entries.add(TextElement.builder()
                .text(builder.name)
                .position(x, offsetY)
                .textScale(builder.titleScale)
                .medium(medium)
                .background(background)
                .textAlignment(textAlignment)
                .build(parent)
        );

        offsetY += builder.offsetAmount;

        for (String line : content) {
            entries.add(TextElement.builder()
                    .text(line)
                    .position(x, offsetY)
                    .textScale(contentScale)
                    .medium(medium)
                    .background(background)
                    .textAlignment(textAlignment)
                    .build(parent)
            );
            offsetY += offsetAmount;
        }
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        for (TextElement entry : entries) {
            entry.onRender(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        for (TextElement entry : entries) {
            entry.onClick(mouseX, mouseY, mouseButton);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String title;
        private String[] content;
        private float x, y;
        private float width = 120, height = 0;
        private float offsetAmount = 10;
        private AbstractScreen parent;
        private TextScale titleScale = TextScale.LARGE;
        private TextScale contentScale = TextScale.NORMAL;
        private boolean background = true;
        private boolean medium = false;
        private TextAlignment textAlignment = TextAlignment.TOP_LEFT;

        public Builder title(String title) {
            this.title = title;
            this.name = title;
            return this;
        }

        public Builder content(String[] content, float offsetAmount) {
            this.content = content;
            this.offsetAmount = offsetAmount;
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

        public Builder size(float width) {
            this.width = width;
            return this;
        }

        public Builder titleScale(TextScale scale) {
            this.titleScale = scale;
            return this;
        }

        public Builder contentScale(TextScale scale) {
            this.contentScale = scale;
            return this;
        }

        public Builder background(boolean background) {
            this.background = background;
            return this;
        }

        public Builder medium(boolean medium) {
            this.medium = medium;
            return this;
        }

        public Builder textAlignment(TextAlignment alignment) {
            this.textAlignment = alignment;
            return this;
        }

        public ListElement build(AbstractScreen parent) {
            this.parent = parent;

            if (title == null || content == null) {
                throw new IllegalStateException("ListElement requires a title and content");
            }

            return new ListElement(this);
        }
    }
}
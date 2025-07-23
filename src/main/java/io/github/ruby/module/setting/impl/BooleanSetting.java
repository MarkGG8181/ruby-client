package io.github.ruby.module.setting.impl;

import io.github.ruby.module.setting.AbstractSetting;

import java.util.function.Consumer;

public class BooleanSetting extends AbstractSetting<Boolean> {
    private BooleanSetting(Builder builder) {
        super(builder.name, builder.description, builder.value);
        this.changeListener = builder.changeListener;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name, description = "";
        private Consumer<Boolean> changeListener = null;
        private boolean value;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder value(boolean value) {
            this.value = value;
            return this;
        }

        public Builder onChange(Consumer<Boolean> changeListener) {
            this.changeListener = changeListener;
            return this;
        }

        public BooleanSetting build() {
            return new BooleanSetting(this);
        }
    }
}

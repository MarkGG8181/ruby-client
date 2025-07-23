package io.github.ruby.module.setting.impl;

import io.github.ruby.module.setting.AbstractSetting;

import java.util.function.Consumer;

public class NumberSetting extends AbstractSetting<Number> {
    private final Number min, max;

    private NumberSetting(Builder builder) {
        super(builder.name, builder.description, builder.value);
        this.changeListener = builder.changeListener;
        this.min = builder.min;
        this.max = builder.max;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name, description = "";
        private Consumer<Number> changeListener = null;
        private Number value, min, max;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder value(Number value) {
            this.value = value;
            return this;
        }

        public Builder min(Number min) {
            this.min = min;
            return this;
        }

        public Builder max(Number max) {
            this.max = max;
            return this;
        }

        public Builder onChange(Consumer<Number> changeListener) {
            this.changeListener = changeListener;
            return this;
        }

        public NumberSetting build() {
            return new NumberSetting(this);
        }
    }
}

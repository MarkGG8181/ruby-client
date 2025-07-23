package io.github.ruby.module.setting.impl;

import io.github.ruby.module.setting.AbstractSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StringSetting extends AbstractSetting<String> {
    private final String[] values;

    private StringSetting(Builder builder) {
        super(builder.name, builder.description, builder.value);
        this.values = builder.values.toArray(new String[0]);
        this.changeListener = builder.changeListener;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name, description = "";
        private String value;
        private final List<String> values = new ArrayList<>();
        private Consumer<String> changeListener = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            if (!values.contains(value)) {
                values.add(value);
            }
            return this;
        }

        public Builder otherValues(String... otherValues) {
            for (String val : otherValues) {
                if (!values.contains(val)) {
                    values.add(val);
                }
            }
            return this;
        }

        public Builder onChange(Consumer<String> changeListener) {
            this.changeListener = changeListener;
            return this;
        }

        public StringSetting build() {
            return new StringSetting(this);
        }
    }
}

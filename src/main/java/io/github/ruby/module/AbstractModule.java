package io.github.ruby.module;

import io.github.ruby.util.IUtility;
import org.jspecify.annotations.NonNull;

public abstract class AbstractModule implements IUtility {
    @NonNull private final ModuleInfo info;

    public AbstractModule() {
        this.info = getClass().getAnnotation(ModuleInfo.class);
    }

    private boolean
            enabled = false,
            frozen = false,
            expanded = false;

    public boolean isEnabled() {
        return enabled && !frozen;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
            BUS.register(this);
        } else {
            BUS.unregister(this);
            onDisable();
        }
    }
}
package io.github.ruby.module;

import io.github.ruby.util.use.Icon;

public enum ModuleCategory {
    COMBAT(Icon.SWORDS),
    MOVEMENT(Icon.AIR),
    RENDER(Icon.EYE),
    PLAYER(Icon.PERSON),
    WORLD(Icon.WORLD),
    MISC(Icon.PUZZLE);

    public final Icon icon;
    public boolean expanded = true;

    ModuleCategory(Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
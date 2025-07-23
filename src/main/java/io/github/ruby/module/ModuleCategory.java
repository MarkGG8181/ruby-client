package io.github.ruby.module;

public enum ModuleCategory {
    COMBAT,
    MOVEMENT,
    RENDER,
    PLAYER,
    WORLD,
    MISC;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
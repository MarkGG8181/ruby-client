package io.github.ruby.util;

public interface IConstants {
    String NAME = "Ruby";
    String ID = NAME.toLowerCase();
    String VERSION = "1.0.0";
    String[] CREATORS = {"desync_lord"};
    String JOINED_CREATORS = String.join(",", CREATORS);

    String TITLE = String.format("%s %s - made by %s", NAME, VERSION, JOINED_CREATORS);

    String[] CHANGES = {
            "§a[+]§f Main menu",
            "§a[+]§f UI system",
            "§a[+]§f Shaders",
            "§a[+]§f Font renderer"
    };
}
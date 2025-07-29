package io.github.ruby.screen;

import io.github.ruby.storage.impl.FontStorage;
import io.github.ruby.util.game.font.impl.CustomFontRenderer;

public interface IFonts {
    CustomFontRenderer REGULAR_16 = FontStorage.INSTANCE.getFont("Roboto-Regular", 16);
    CustomFontRenderer MEDIUM_16 = FontStorage.INSTANCE.getFont("Roboto-Medium", 16);
    CustomFontRenderer REGULAR_18 = FontStorage.INSTANCE.getFont("Roboto-Regular", 18);
    CustomFontRenderer MEDIUM_18 = FontStorage.INSTANCE.getFont("Roboto-Medium", 18);
    CustomFontRenderer REGULAR_20 = FontStorage.INSTANCE.getFont("Roboto-Regular", 20);
    CustomFontRenderer MEDIUM_20 = FontStorage.INSTANCE.getFont("Roboto-Medium", 20);

    CustomFontRenderer REGULAR_40 = FontStorage.INSTANCE.getFont("Roboto-Regular", 40);

    CustomFontRenderer ICON_22 = FontStorage.INSTANCE.getFont("RubyIcons-Regular", 22);
}
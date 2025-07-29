package io.github.ruby.module.impl.render;

import io.github.ruby.module.AbstractModule;
import io.github.ruby.module.ModuleCategory;
import io.github.ruby.module.ModuleInfo;
import io.github.ruby.screen.impl.ClickGuiScreen;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "ClickGui", description = "Renders a clicky gui.", category = ModuleCategory.RENDER)
public class ClickGuiModule extends AbstractModule {
    public ClickGuiModule() {
        this.key = Keyboard.KEY_RSHIFT;
    }

    private ClickGuiScreen clickGuiScreen;

    @Override
    public void onEnable() {
        if (clickGuiScreen == null) {
            clickGuiScreen = new ClickGuiScreen();
        }

        mc.displayGuiScreen(clickGuiScreen);
        setEnabled(false);
    }
}
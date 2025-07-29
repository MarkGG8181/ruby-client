package io.github.ruby.processor.impl;

import com.google.common.eventbus.Subscribe;
import io.github.ruby.event.impl.UpdateCameraAndRenderEvent;
import io.github.ruby.processor.AbstractProcessor;
import io.github.ruby.screen.impl.MainMenuScreen;
import io.github.ruby.util.IMinecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class ScreenProcessor extends AbstractProcessor implements IMinecraft {
    @Override
    public void init() {
        super.init();
        mc.displayGuiScreen(new MainMenuScreen());
    }

    @Subscribe
    public void onRenderFrame(UpdateCameraAndRenderEvent ignoredEvent) {
        if (mc.currentScreen instanceof GuiMainMenu) {
            mc.displayGuiScreen(new MainMenuScreen());
        }
    }
}
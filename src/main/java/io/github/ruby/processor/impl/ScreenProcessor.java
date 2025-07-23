package io.github.ruby.processor.impl;

import com.google.common.eventbus.Subscribe;
import io.github.ruby.event.impl.*;
import io.github.ruby.processor.AbstractProcessor;
import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.impl.MainMenuScreen;
import io.github.ruby.util.IMinecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ScreenProcessor extends AbstractProcessor implements IMinecraft {
    private AbstractScreen currentScreen;

    @Override
    public void init() {
        super.init();
        setCurrentScreen(new MainMenuScreen());
    }

    public void setCurrentScreen(AbstractScreen screen) {
        if (this.currentScreen != null) {
            this.currentScreen.onClose();
        }

        this.currentScreen = screen;

        if (this.currentScreen != null) {
            mc.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(mc);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            this.currentScreen.setSize(width, height);
        } else {
            mc.setIngameFocus();
        }
    }

    @Subscribe
    public void onRenderFrame(UpdateCameraAndRenderEvent event) {
        if (currentScreen == null && mc.currentScreen instanceof GuiMainMenu) {
            setCurrentScreen(new MainMenuScreen());
        }

        if (currentScreen != null)
            currentScreen.onRender(event.mouseX, event.mouseY, event.partialTicks);
    }

    @Subscribe
    public void onLoop(RunTickEvent event) {
        if (currentScreen != null) {
            currentScreen.onLoop();
        }
    }

    @Subscribe
    public void onMouse(MouseInputEvent event) {
        if (currentScreen != null) {
            currentScreen.onMouse();
        } else {
            mc.setIngameFocus();
        }
    }

    @Subscribe
    public void onKeyboard(KeyboardInputEvent event) {
        if (currentScreen != null) {
            currentScreen.onType(Keyboard.getEventCharacter(), event.key);
        }
    }

    @Subscribe
    public void onResizeWindow(ResizeWindowEvent event) {
        if (currentScreen != null) {
            ScaledResolution scaledresolution = new ScaledResolution(mc);
            currentScreen.onResize(currentScreen.width, currentScreen.height, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        }
    }
}
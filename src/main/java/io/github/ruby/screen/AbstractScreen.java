package io.github.ruby.screen;

import io.github.ruby.processor.impl.ScreenProcessor;
import io.github.ruby.screen.element.AbstractElement;
import io.github.ruby.storage.impl.ProcessorStorage;
import io.github.ruby.util.IMinecraft;
import io.github.ruby.util.game.shader.ShaderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScreen implements IMinecraft, IFonts, IColors {
    public final String name;
    public int width, height;

    private int mouseButton;
    private long lastClick;

    public final List<AbstractElement> children = new ArrayList<>();

    public AbstractScreen(String name) {
        this.name = name;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        onInit();
    }

    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        setSize(newWidth, newHeight);
    }

    public void onInit() {
        children.clear();
    }

    public void onClose() {}

    public void onLoop() {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.onMouse();
            }
        }

        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.onKeyboard();
            }
        }
    }

    public void onMouse() {
        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        int mouseButton = Mouse.getEventButton();

        if (Mouse.getEventButtonState()) {
            this.mouseButton = mouseButton;
            this.lastClick = Minecraft.getSystemTime();
            onClick(mouseX, mouseY, mouseButton);
        } else if (mouseButton != -1) {
            this.mouseButton = -1;
            onRelease(mouseX, mouseY, mouseButton);
        } else if (this.mouseButton != -1 && this.lastClick > 0L) {
            onDrag(mouseX, mouseY, this.mouseButton, Minecraft.getSystemTime() - this.lastClick);
        }
    }

    public void onKeyboard() {
        if (Keyboard.getEventKeyState()) {
            onType(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }

        mc.dispatchKeypresses();
    }

    public void onRender(int mouseX, int mouseY, float partialTicks) {
        ShaderUtil.rect(0, 0, width, height, new Color(20, 20, 20));
        children.forEach(c -> c.onRender(mouseX, mouseY, partialTicks));
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        children.forEach(c -> c.onClick(mouseX, mouseY, mouseButton));
    }

    public void onRelease(int mouseX, int mouseY, int mouseButton) {}
    public void onDrag(int mouseX, int mouseY, int mouseButton, long mouseTime) {}

    public void onType(char character, int key) {
        if (key == Keyboard.KEY_ESCAPE) {
            ProcessorStorage.INSTANCE.getByClass(ScreenProcessor.class).setCurrentScreen(null);
        }
    }
}
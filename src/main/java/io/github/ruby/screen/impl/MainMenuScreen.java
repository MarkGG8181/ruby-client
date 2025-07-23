package io.github.ruby.screen.impl;

import io.github.ruby.processor.impl.ScreenProcessor;
import io.github.ruby.screen.AbstractScreen;
import io.github.ruby.screen.element.impl.ImageElement;
import io.github.ruby.screen.element.impl.ListElement;
import io.github.ruby.screen.element.option.TextScale;
import io.github.ruby.screen.element.impl.ButtonElement;
import io.github.ruby.storage.impl.ProcessorStorage;
import io.github.ruby.util.IConstants;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;

public class MainMenuScreen extends AbstractScreen {
    public MainMenuScreen() {
        super("Main Menu");
    }

    @Override
    public void onInit() {
        super.onInit();

        float offset = 5;

        children.add(ImageElement.builder()
                .texture("icons8-ruby-100.png")
                .position(5, offset)
                .size(50, 50)
                .background(false)
                .build(this)
        );

        offset += 50;

        children.add(ButtonElement.builder()
                .text("Singleplayer")
                .position(8, offset)
                .onClick(() -> {
                    ProcessorStorage.INSTANCE.getByClass(ScreenProcessor.class).setCurrentScreen(null);
                    mc.displayGuiScreen(new GuiSelectWorld(null));
                })
                .build(this)
        );

        offset += 14;
        children.add(ButtonElement.builder()
                .text("Multiplayer")
                .position(8, offset)
                .onClick(() -> {
                    ProcessorStorage.INSTANCE.getByClass(ScreenProcessor.class).setCurrentScreen(null);
                    mc.displayGuiScreen(new GuiMultiplayer(null));
                })
                .build(this)
        );

        offset += 14;
        children.add(ButtonElement.builder()
                .text("Options")
                .position(8, offset)
                .onClick(() -> {
                    ProcessorStorage.INSTANCE.getByClass(ScreenProcessor.class).setCurrentScreen(null);
                    mc.displayGuiScreen(new GuiOptions(null, mc.gameSettings));
                })
                .build(this)
        );

        offset += 14;
        children.add(ButtonElement.builder()
                .text("Alts")
                .position(8, offset)
                .build(this)
        );

        offset += 14;
        children.add(ButtonElement.builder()
                .text("Quit")
                .position(8, offset)
                .onClick(() -> System.exit(0))
                .build(this)
        );

        offset += 15 + 10;
        children.add(ListElement.builder()
                .title("Changelog")
                .position(8, offset)
                .content(IConstants.CHANGES, 10)
                .background(false)
                .titleScale(TextScale.LARGE)
                .contentScale(TextScale.NORMAL)
                .build(this)
        );

        children.add(ImageElement.builder()
                .texture("anime-girl.png")
                .position(width - 190.5f / 1.3f, height - 328 / 1.3f)
                .size(190.5f / 1.3f, 328 / 1.3f)
                .background(false)
                .build(this)
        );
    }
}
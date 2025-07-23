package io.github.ruby;

import io.github.ruby.storage.impl.FontStorage;
import io.github.ruby.storage.impl.ModuleStorage;
import io.github.ruby.storage.impl.ProcessorStorage;
import io.github.ruby.util.IConstants;
import io.github.ruby.util.IUtility;
import io.github.ruby.util.game.shader.ShaderUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL45;

public enum Ruby implements IUtility, IConstants {
    INSTANCE;

    public void start() {
        Display.setTitle(TITLE);
        FontStorage.INSTANCE.init();
        ModuleStorage.INSTANCE.init();
        ProcessorStorage.INSTANCE.init();
        LOGGER.info("Started {} v{}", NAME, VERSION);
    }

    public void stop() {
        ShaderUtil.TEXTURE_MAP.values().forEach(GL45::glDeleteTextures);
        ShaderUtil.TEXTURE_MAP.clear();
    }
}
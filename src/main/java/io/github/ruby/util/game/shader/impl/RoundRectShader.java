package io.github.ruby.util.game.shader.impl;

import io.github.ruby.util.game.shader.AbstractShader;
import io.github.ruby.util.game.shader.ShaderUtil;
import io.github.ruby.util.use.Param;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL45;

import java.awt.*;

public class RoundRectShader extends AbstractShader {
    public RoundRectShader() {
        super("roundrect.frag");
    }

    @Override
    public void draw(Param param) {
        use();
        setup(param);

        ShaderUtil.draw(param.x(), param.y(), param.width(), param.height());

        unuse();
    }

    @Override
    public void setup(Param param) {
        Color color = param.colors()[0];

        ScaledResolution sr = new ScaledResolution(mc);
        setUniformsFloats("location", param.x() * sr.getScaleFactor(), (mc.displayHeight - (param.height() * sr.getScaleFactor())) - (param.y() * sr.getScaleFactor()));
        setUniformsFloats("rectSize", param.width() * sr.getScaleFactor(), param.height() * sr.getScaleFactor());
        setUniformsFloats("radius", param.radius() * sr.getScaleFactor());
        setUniformsFloats("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }
}

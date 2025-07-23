package io.github.ruby.util.game.shader.impl;

import io.github.ruby.util.game.shader.AbstractShader;
import io.github.ruby.util.game.shader.ShaderUtil;
import io.github.ruby.util.use.Param;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL45;

import java.awt.*;

public class HGradientRectShader extends AbstractShader {
    public HGradientRectShader() {
        super("hgradrect.frag");
    }

    @Override
    public void draw(Param param) {
        use();
        setup(param);
        GlStateManager.enableBlend();
        GL45.glBlendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA);
        ShaderUtil.draw(param.x() - 1, param.y() - 1, param.width() + 2, param.height() + 2);
        unuse();
        GlStateManager.disableBlend();
    }

    @Override
    public void setup(Param param) {
        Color colorA = param.colors()[0];
        Color colorB = param.colors()[1];

        ScaledResolution sr = new ScaledResolution(mc);
        setUniformsFloats("location", param.x() * sr.getScaleFactor(), (mc.displayHeight - (param.height() * sr.getScaleFactor())) - (param.y() * sr.getScaleFactor()));
        setUniformsFloats("rectSize", param.width() * sr.getScaleFactor(), param.height() * sr.getScaleFactor());
        setUniformsFloats("radius", param.radius() * sr.getScaleFactor());
        setUniformsFloats("colorA", colorA.getRed() / 255f, colorA.getGreen() / 255f, colorA.getBlue() / 255f, colorA.getAlpha() / 255f);
        setUniformsFloats("colorB", colorB.getRed() / 255f, colorB.getGreen() / 255f, colorB.getBlue() / 255f, colorB.getAlpha() / 255f);
    }
}

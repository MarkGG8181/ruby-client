package io.github.ruby.util.game.shader;

import io.github.ruby.Ruby;
import io.github.ruby.util.IUtility;
import io.github.ruby.util.game.shader.impl.HGradientRectShader;
import io.github.ruby.util.game.shader.impl.RoundImageShader;
import io.github.ruby.util.game.shader.impl.RoundRectShader;
import io.github.ruby.util.game.shader.impl.VGradientRectShader;
import io.github.ruby.util.use.Param;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShaderUtil implements IUtility {
    public static final HashMap<String, Integer> TEXTURE_MAP = new HashMap<>();

    public static final HGradientRectShader H_GRADIENT_RECT_SHADER = new HGradientRectShader();
    public static final VGradientRectShader V_GRADIENT_RECT_SHADER = new VGradientRectShader();
    public static final RoundRectShader ROUND_RECT_SHADER = new RoundRectShader();
    public static final RoundImageShader ROUND_IMAGE_SHADER = new RoundImageShader();

    public static String readShaderFile(String filePath) {
        filePath = "/assets/ruby/shaders/" + filePath;

        try (InputStream is = Ruby.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException("Shader file not found: " + filePath);
            }

            return new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read shader file: " + filePath, e);
        }
    }

    public static void draw(float x, float y, float width, float height) {
        GL45.glBegin(GL45.GL_QUADS);
        GL45.glTexCoord2f(0, 0);
        GL45.glVertex2f(x, y);
        GL45.glTexCoord2f(0, 1);
        GL45.glVertex2f(x, y + height);
        GL45.glTexCoord2f(1, 1);
        GL45.glVertex2f(x + width, y + height);
        GL45.glTexCoord2f(1, 0);
        GL45.glVertex2f(x + width, y);
        GL45.glEnd();
    }

    public static int uploadBufferedImageToGL(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y = image.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                buffer.put((byte) (pixel & 0xFF));         // Blue
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }
        }
        buffer.flip();

        int texId = GL45.glGenTextures();
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, texId);

        GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_LINEAR);
        GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_LINEAR);

        GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA8, image.getWidth(), image.getHeight(),
                0, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, buffer);

        return texId;
    }

    public static void hGrad(float x, float y, float width, float height, float radius, Color colorA, Color colorB) {
        H_GRADIENT_RECT_SHADER.draw(new Param(x, y, width, height, radius, 0, colorA, colorB));
    }

    public static void vGrad(float x, float y, float width, float height, float radius, Color colorA, Color colorB) {
        V_GRADIENT_RECT_SHADER.draw(new Param(x, y, width, height, radius, 0, colorA, colorB));
    }

    public static void rect(float x, float y, float width, float height, Color color) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA);
        rect(x, y, width, height, 0, color);
    }

    public static void rect(float x, float y, float width, float height, float radius, Color color) {
        ROUND_RECT_SHADER.draw(new Param(x, y, width, height, radius, 0, color));
    }

    public static void image(int texId, float x, float y, float width, float height, float radius) {
        ROUND_IMAGE_SHADER.draw(new Param(x, y, width, height, radius, texId, Color.WHITE));
    }

    public static void image(String imageFileName, float x, float y, float width, float height, float radius) {
        int texId = 0;

        if (TEXTURE_MAP.containsKey(imageFileName)) {
            texId = TEXTURE_MAP.get(imageFileName);
        } else {
            try {
                texId = uploadBufferedImageToGL(ImageIO.read(Objects.requireNonNull(
                        ShaderUtil.class.getResourceAsStream("/assets/ruby/images/" + imageFileName)
                )));
                TEXTURE_MAP.put(imageFileName, texId);
            } catch (IOException e) {
                LOGGER.error("Failed to load resource {} to buffer", imageFileName, e);
            }
        }

        ROUND_IMAGE_SHADER.draw(new Param(x, y, width, height, radius, texId, Color.WHITE));
    }
}
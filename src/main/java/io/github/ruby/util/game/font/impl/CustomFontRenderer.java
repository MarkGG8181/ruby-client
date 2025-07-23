package io.github.ruby.util.game.font.impl;

import io.github.ruby.util.game.font.FontCharData;
import io.github.ruby.util.game.font.FontTextureData;
import io.github.ruby.util.game.font.IFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CustomFontRenderer implements IFontRenderer {
    private final Font font;
    private final boolean antiAlias;
    private final boolean fractionalMetrics;

    private final FontCharData[]
            regularData,
            boldData,
            italicsData;

    private final int[] colorCodes;

    public CustomFontRenderer(ConcurrentLinkedQueue<FontTextureData> textureQueue, Font font) {
        this(textureQueue, font, 256);
    }

    public CustomFontRenderer(ConcurrentLinkedQueue<FontTextureData> textureQueue, Font font, int characterCount) {
        this(textureQueue, font, characterCount, true);
    }

    public CustomFontRenderer(ConcurrentLinkedQueue<FontTextureData> textureQueue, Font font, boolean antiAlias) {
        this(textureQueue, font, 256, antiAlias);
    }

    public CustomFontRenderer(ConcurrentLinkedQueue<FontTextureData> textureQueue, Font font, int characterCount, boolean antiAlias) {
        this.colorCodes = new int[32];
        this.font = font;
        this.fractionalMetrics = true;
        this.antiAlias = antiAlias;
        int[] regularTexturesIds = new int[characterCount];
        int[] boldTexturesIds = new int[characterCount];
        int[] italicTexturesIds = new int[characterCount];

        for (int i = 0; i < characterCount; ++i) {
            regularTexturesIds[i] = GL45.glGenTextures();
            boldTexturesIds[i] = GL45.glGenTextures();
            italicTexturesIds[i] = GL45.glGenTextures();
        }

        this.regularData = setup(new FontCharData[characterCount], regularTexturesIds, textureQueue, 0);
        this.boldData = setup(new FontCharData[characterCount], boldTexturesIds, textureQueue, 1);
        this.italicsData = setup(new FontCharData[characterCount], italicTexturesIds, textureQueue, 2);
    }

    @Override
    public String toString() {
        return font.getName() + " " + font.getSize();
    }

    @Override
    public int drawString(String text, float x, float y, int color) {
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        return drawString(text, x, y, color, false);
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();

        GL45.glTranslated(0.5, 0.5, 0.0);
        drawString(text, x, y, color, true);
        GL45.glTranslated(-0.5, -0.5, 0.0);
        drawString(text, x, y, color, false);

        return (int) x;
    }

    @Override
    public int drawString(String text, float x, float y, int color, boolean shadow) {
        if (text.isEmpty()) {
            return 0;
        }

        x = Math.round(x * 10.0F) / 10.0F;
        y = Math.round(y * 10.0F) / 10.0F;

        GL45.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA);

        x -= 2.0f;
        y -= 2.0f;
        x += 0.5f;
        y += 0.5f;
        x *= 2.0f;
        y *= 2.0f;

        FontCharData[] characterData = this.regularData;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;

        int length = text.length();
        double multiplier = 255.0 * (shadow ? 4 : 1);

        Color c = new Color(color);
        GL45.glColor4d(c.getRed() / multiplier, c.getGreen() / multiplier, c.getBlue() / multiplier, (color >> 24 & 0xFF) / 255.0);

        for (int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            char previous = (i > 0) ? text.charAt(i - 1) : '.';

            if (previous != '§') {
                if (character == '§') {
                    try {
                        int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                        if (index < 16) {
                            obfuscated = false;
                            strikethrough = false;
                            underlined = false;
                            characterData = this.regularData;

                            if (index < 0) {
                                index = 15;
                            }

                            if (shadow) {
                                index += 16;
                            }

                            int textColor = this.colorCodes[index];
                            GL45.glColor4d((textColor >> 16) / 255.0, (textColor >> 8 & 0xFF) / 255.0, (textColor & 0xFF) / 255.0, (color >> 24 & 0xFF) / 255.0);
                        } else if (index <= 20) {
                            switch (index) {
                                case 16:
                                    obfuscated = true;
                                    break;

                                case 17:
                                    characterData = this.boldData;
                                    break;

                                case 18:
                                    strikethrough = true;
                                    break;

                                case 19:
                                    underlined = true;
                                    break;

                                case 20:
                                    characterData = this.italicsData;
                                    break;
                            }
                        } else {
                            obfuscated = false;
                            strikethrough = false;
                            underlined = false;
                            characterData = this.regularData;
                            GL45.glColor4d((shadow ? 0.25 : 1.0), (shadow ? 0.25 : 1.0), (shadow ? 0.25 : 1.0), (color >> 24 & 0xFF) / 255.0);
                        }

                    } catch (StringIndexOutOfBoundsException ignored) {
                    }
                } else if (character <= 'ÿ') {
                    if (obfuscated) {
                        character += 1;
                    }

                    drawChar(character, characterData, x, y);
                    FontCharData charData = characterData[character];

                    if (strikethrough) {
                        drawLine(new Vector2f(0.0f, charData.height() / 2.0f), new Vector2f(charData.width(), charData.height() / 2.0f));
                    }

                    if (underlined) {
                        drawLine(new Vector2f(0.0f, charData.height() - 15.0f), new Vector2f(charData.width(), charData.height() - 15.0f));
                    }

                    x += charData.width() - 8.0f;
                }
            }
        }

        GL45.glPopMatrix();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GlStateManager.resetColor();

        return (int) x;
    }

    @Override
    public int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow) {
        return this.renderString(text, (float) x, (float) y, color, dropShadow);
    }

    @Override
    public int renderString(String text, float x, float y, int color, boolean dropShadow) {
        return drawString(text, x, y, color, dropShadow);
    }

    @Override
    public int getWidth(String text) {
        int width = 0;
        FontCharData[] characterData = this.regularData;

        //remove color codes from the text
        String strippedText = StringUtils.stripControlCodes(text);

        try {
            for (int length = strippedText.length(), i = 0; i < length; ++i) {
                char character = strippedText.charAt(i);

                FontCharData charData = characterData[character];
                width += (int) ((charData.width() - 8) / 2);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return getWidth("A");
        }

        return width + 2;
    }

    public int getHeight(String text) {
        int height = 0;

        for (int length = text.length(), i = 0; i < length; ++i) {
            char character = text.charAt(i);
            FontCharData charData = this.regularData[character];
            height = (int) Math.max(height, charData.height());
        }

        return height / 2 - 2;
    }

    @Override
    public int getHeight() {
        return getHeight("I");
    }

    private FontCharData[] setup(FontCharData[] characterData, int[] texturesIds, ConcurrentLinkedQueue<FontTextureData> textureQueue, int type) {
        generateColors();

        Font font = this.font.deriveFont(type);
        BufferedImage utilityImage = new BufferedImage(1, 1, 2);

        Graphics2D utilityGraphics = (Graphics2D) utilityImage.getGraphics();
        utilityGraphics.setFont(font);

        FontMetrics fontMetrics = utilityGraphics.getFontMetrics();

        for (int index = 0; index < characterData.length; ++index) {
            char character = (char) index;

            Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), utilityGraphics);
            float width = (float) characterBounds.getWidth() + 8.0f;
            float height = (float) characterBounds.getHeight();

            BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_double_int(width), MathHelper.ceiling_double_int(height), 2);

            Graphics2D graphics = (Graphics2D) characterImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            graphics.setColor(Color.WHITE);

            if (this.antiAlias) {
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            }

            graphics.drawString(String.valueOf(character), 4, fontMetrics.getAscent());
            int textureId = texturesIds[index];

            createTexture(textureId, characterImage, textureQueue);
            characterData[index] = new FontCharData(character, (float) characterImage.getWidth(), (float) characterImage.getHeight(), textureId);
        }
        return characterData;
    }

    private void drawChar(char character, FontCharData[] characterData, float x, float y) {
        if (character >= characterData.length) {
            return;
        }

        FontCharData charData = characterData[character];
        charData.bind();

        GL45.glBegin(GL45.GL_TRIANGLE_FAN);
        GL45.glTexCoord2f(0.0f, 0.0f);
        GL45.glVertex2d(x, y);
        GL45.glTexCoord2f(0.0f, 1.0f);
        GL45.glVertex2d(x, y + charData.height());
        GL45.glTexCoord2f(1.0f, 1.0f);
        GL45.glVertex2d(x + charData.width(), y + charData.height());
        GL45.glTexCoord2f(1.0f, 0.0f);
        GL45.glVertex2d(x + charData.width(), y);
        GL45.glEnd();
    }

    private void drawLine(Vector2f start, Vector2f end) {
        GL45.glDisable(GL45.GL_TEXTURE_2D);
        GL45.glLineWidth(3.0f);
        GL45.glBegin(GL45.GL_LINES);
        GL45.glVertex2f(start.x, start.y);
        GL45.glVertex2f(end.x, end.y);
        GL45.glEnd();
        GL45.glEnable(GL45.GL_TEXTURE_2D);
    }

    private void generateColors() {
        for (int i = 0; i < 32; ++i) {
            int thingy = (i >> 3 & 0x1) * 85;
            int red = (i >> 2 & 0x1) * 170 + thingy;
            int green = (i >> 1 & 0x1) * 170 + thingy;
            int blue = (i & 0x1) * 170 + thingy;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCodes[i] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }

    private void createTexture(int textureId, BufferedImage image, ConcurrentLinkedQueue<FontTextureData> textureQueue) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) (pixel >> 16 & 0xFF));
                buffer.put((byte) (pixel >> 8 & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) (pixel >> 24 & 0xFF));
            }
        }

        buffer.flip();
        textureQueue.add(new FontTextureData(textureId, image.getWidth(), image.getHeight(), buffer));
    }
}
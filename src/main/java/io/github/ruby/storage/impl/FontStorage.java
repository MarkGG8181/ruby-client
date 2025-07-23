package io.github.ruby.storage.impl;

import io.github.ruby.util.IConstants;
import io.github.ruby.util.IUtility;
import io.github.ruby.util.game.font.FontTextureData;
import io.github.ruby.util.game.font.impl.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL45;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FontStorage implements IUtility {
    public static final FontStorage INSTANCE = new FontStorage();

    private final HashMap<String, CustomFontRenderer> fonts = new HashMap<>();
    public final Map<String, List<String>> availableFonts = new HashMap<>();

    private static final IResourceManager _resourceManager = Minecraft.getMinecraft().getResourceManager();
    private static final String _fontDirectory = "fonts/";

    private final ConcurrentLinkedQueue<FontTextureData> textureQueue = new ConcurrentLinkedQueue<>();
    private final CustomFontRenderer defaultFont = new CustomFontRenderer(textureQueue, new Font("Roboto-Regular", Font.PLAIN, 16));

    public void init() {
        LOGGER.info("Set the fallback font to: {}", defaultFont);

        try {
            for (String family : ResourceStorage.INSTANCE.get(_fontDirectory)) {
                if (!family.contains(".") || family.lastIndexOf('.') <= 0) {
                    String familyName = family.split("-")[0];

                    final List<String> types = new LinkedList<>();

                    for (String type : ResourceStorage.INSTANCE.get(_fontDirectory + familyName + "/")) {
                        String typeName = type.replaceAll("\\.(ttf|otf)$", "").split("-")[1];
                        types.add(typeName);
                    }

                    availableFonts.put(family, types);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Finished finding fonts");
    }

    public CustomFontRenderer getFont(final String key, final int size) {
        String newKey = key + " " + size;
        if (this.fonts.containsKey(newKey)) {
            return this.fonts.get(newKey);
        } else {
            try {
                final String[] split = newKey.split(" "), nameSplit = split[0].split("-");
                final String family = nameSplit[0], style = nameSplit[1];

                boolean success = false;

                for (String extension : new String[]{".ttf", ".otf"}) {
                    final String path = String.format("%s/%s-%s%s", family, family, style, extension);
                    if (loadFont(path, family + "-" + style, new int[]{size})) {
                        success = true;
                        break;
                    }
                }

                while (!textureQueue.isEmpty()) {
                    final FontTextureData textureData = textureQueue.poll();

                    GlStateManager.bindTexture(textureData.textureId());
                    GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_NEAREST);
                    GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_NEAREST);
                    GL45.glTexImage2D(GL45.GL_TEXTURE_2D, GL45.GL_ZERO, GL45.GL_RGBA, textureData.width(), textureData.height(), GL45.GL_ZERO, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, textureData.buffer());
                }

                if (success && fonts.containsKey(newKey)) {
                    return fonts.get(newKey);
                } else {
                    LOGGER.error("Failed to create font {}", newKey);
                    return defaultFont;
                }
            } catch (Exception e) {
                LOGGER.error("Failed to create font {}", newKey, e);
                return defaultFont;
            }
        }
    }

    private boolean loadFont(String fontPath, String fontName, int[] sizes) {
        try {
            for (int size : sizes) {
                Font awtFont = Font
                        .createFont(Font.TRUETYPE_FONT, _resourceManager
                                .getResource(new ResourceLocation(IConstants.ID, _fontDirectory + fontPath))
                                .getInputStream());

                awtFont = awtFont.deriveFont(Font.PLAIN, (float) size);
                fonts.put(fontName + " " + size, new CustomFontRenderer(textureQueue, awtFont));

                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load font {}", fontPath, e);
        }

        return false;
    }
}
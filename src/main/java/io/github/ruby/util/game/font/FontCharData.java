package io.github.ruby.util.game.font;

import org.lwjgl.opengl.GL45;

public record FontCharData(char character, float width, float height, int textureId) {
    public void bind() {
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, this.textureId);
    }
}
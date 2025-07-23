package io.github.ruby.util.game.font;

import java.nio.ByteBuffer;

public record FontTextureData(int textureId, int width, int height, ByteBuffer buffer) {
}
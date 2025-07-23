package io.github.ruby.util.game.shader;

import io.github.ruby.util.IMinecraft;
import io.github.ruby.util.IUtility;
import io.github.ruby.util.use.Param;
import org.lwjgl.opengl.GL45;

public abstract class AbstractShader implements IUtility, IMinecraft {
    private final int programId;

    public AbstractShader(String frag) {
        int vertexShaderId = compileShader(ShaderUtil.readShaderFile("vertex.vert"), GL45.GL_VERTEX_SHADER);
        int fragmentShaderId = compileShader(ShaderUtil.readShaderFile(frag), GL45.GL_FRAGMENT_SHADER);

        programId = GL45.glCreateProgram();
        GL45.glAttachShader(programId, vertexShaderId);
        GL45.glAttachShader(programId, fragmentShaderId);
        GL45.glLinkProgram(programId);

        // Check for linking errors
        if (GL45.glGetProgrami(programId, GL45.GL_LINK_STATUS) == GL45.GL_FALSE) {
            LOGGER.error("Shader program linking failed: {}", GL45.glGetProgramInfoLog(programId));
            GL45.glDeleteProgram(programId);
            throw new RuntimeException("Failed to link shader program.");
        }

        // Detach and delete shaders after linking (they are now part of the program)
        GL45.glDetachShader(programId, vertexShaderId);
        GL45.glDetachShader(programId, fragmentShaderId);
        GL45.glDeleteShader(vertexShaderId);
        GL45.glDeleteShader(fragmentShaderId);
    }

    private int compileShader(String shaderSource, int shaderType) {
        int shaderId = GL45.glCreateShader(shaderType);
        GL45.glShaderSource(shaderId, shaderSource);
        GL45.glCompileShader(shaderId);

        if (GL45.glGetShaderi(shaderId, GL45.GL_COMPILE_STATUS) == GL45.GL_FALSE) {
            LOGGER.error("Shader compilation failed ({}):\n{}", shaderType == GL45.GL_VERTEX_SHADER ? "Vertex" : "Fragment", GL45.glGetShaderInfoLog(shaderId));
            GL45.glDeleteShader(shaderId);
            throw new RuntimeException("Failed to compile shader.");
        }
        return shaderId;
    }

    public abstract void draw(Param param);
    public abstract void setup(Param param);

    public void use() {
        GL45.glUseProgram(programId);
    }

    public void unuse() {
        GL45.glUseProgram(0);
    }

    public void delete() {
        GL45.glDeleteProgram(programId);
    }

    public void setUniformsFloats(String name, float... args) {
        int loc = GL45.glGetUniformLocation(programId, name);

        if (args.length > 1) {
            if (args.length > 2) {
                if (args.length > 3)
                    GL45.glUniform4f(loc, args[0], args[1], args[2], args[3]);
                else
                    GL45.glUniform3f(loc, args[0], args[1], args[2]);
            } else
                GL45.glUniform2f(loc, args[0], args[1]);
        } else
            GL45.glUniform1f(loc, args[0]);
    }

    public void setUniformsInts(String name, int... args) {
        int loc = GL45.glGetUniformLocation(programId, name);

        if (args.length > 1) {
            if (args.length > 2) {
                if (args.length > 3)
                    GL45.glUniform4i(loc, args[0], args[1], args[2], args[3]);
                else
                    GL45.glUniform3i(loc, args[0], args[1], args[2]);
            } else
                GL45.glUniform2i(loc, args[0], args[1]);
        } else
            GL45.glUniform1i(loc, args[0]);
    }

    public void setUniformBoolean(String name, boolean value) {
        int loc = GL45.glGetUniformLocation(programId, name);
        GL45.glUniform1i(loc, value ? 1 : 0);
    }
}
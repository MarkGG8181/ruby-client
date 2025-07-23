#version 120

uniform vec2 location;
uniform vec2 rectSize;
uniform float radius;

uniform vec4 color;

float roundSDF(vec2 centerPos, vec2 size, float radius) {
    return length(max(abs(centerPos) - size, 0.0)) - radius;
}

void main() {
    vec2 rectHalf = rectSize * .5;

    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;

    gl_FragColor = vec4(color.rgb, smoothedAlpha);
}
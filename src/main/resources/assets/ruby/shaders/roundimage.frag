#version 120

uniform vec2 location;
uniform vec2 rectSize;
uniform float radius;

uniform sampler2D image;

float roundSDF(vec2 p, vec2 size, float radius) {
    return length(max(abs(p) - size, 0.0)) - radius;
}

void main() {
    vec2 fragPos = gl_FragCoord.xy - location;
    vec2 halfSize = rectSize * 0.5;
    vec2 centerPos = fragPos - halfSize;

    // Discard pixels outside the rounded rectangle
    float dist = roundSDF(centerPos, halfSize - radius - 1.0, radius);
    float alpha = 1.0 - smoothstep(0.0, 1.0, dist);
    if (alpha <= 0.0) discard;

    // Convert to UV
    vec2 uv = fragPos / rectSize;
    if (uv.x < 0.0 || uv.x > 1.0 || uv.y < 0.0 || uv.y > 1.0) discard;

    vec4 texColor = texture2D(image, uv);
    gl_FragColor = vec4(texColor.rgb, texColor.a * alpha);
}
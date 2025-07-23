#version 120

uniform vec2 location;
uniform vec2 rectSize;
uniform float radius;

uniform vec4 colorA;
uniform vec4 colorB;

float roundSDF(vec2 pos, vec2 halfSize, float r) {
    vec2 d = abs(pos) - halfSize + r;
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - r;
}

void main() {
    vec2 halfSize = rectSize * 0.5;

    vec2 center = location + halfSize;
    vec2 pos = gl_FragCoord.xy - center;

    float dist = roundSDF(pos, halfSize, radius);
    float alphaMask = smoothstep(0.0, -1.0, dist);

    if (alphaMask <= 0.0) discard;

    vec2 uv = (gl_FragCoord.xy - location) / rectSize;
    uv = clamp(uv, 0.0, 1.0);

    vec4 color = mix(colorA, colorB, uv.x);
    color.a *= alphaMask; // multiply gradient alpha by rounded corner alpha mask

    gl_FragColor = color;
}
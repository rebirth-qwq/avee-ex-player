package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class FractalNoiseEffectElement extends DummyElement {
    public static final String typeName = "FractalNoise";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_fractal_noise);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_evolution;\nuniform float u_scale;\nuniform float u_octaves;\nuniform float u_lacunarity;\nuniform float u_gain;\nuniform float u_warp;\nuniform float u_warpSpeed;\nuniform float u_direction;\nuniform float u_colorR;\nuniform float u_colorG;\nuniform float u_colorB;\nuniform float u_invert;\nuniform float u_blendOriginal;\n\nfloat hash(vec2 p) {\n    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);\n}\n\nfloat smoothNoise(vec2 p) {\n    vec2 i = floor(p);\n    vec2 f = fract(p);\n    f = f * f * (3.0 - 2.0 * f);\n    float a = hash(i);\n    float b = hash(i + vec2(1.0, 0.0));\n    float c = hash(i + vec2(0.0, 1.0));\n    float d = hash(i + vec2(1.0, 1.0));\n    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);\n}\n\nfloat fbm(vec2 p) {\n    float value = 0.0;\n    float amplitude = 0.5;\n    float frequency = 1.0;\n    float lacunarity = u_lacunarity;\n    float gain = u_gain;\n    int octaves = int(floor(u_octaves));\n    for (int i = 0; i < 8; i++) {\n        if (i >= octaves) break;\n        value += amplitude * smoothNoise(p * frequency);\n        amplitude *= gain;\n        frequency *= lacunarity;\n    }\n    return value;\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    float speed = u_evolution * 1.5;\n    float scale = u_scale * 5.0 + 0.5;\n    float warp = u_warp * 0.5;\n    float warpSpeed = u_warpSpeed;\n    \n    float angle = u_direction * 6.2832;\n    vec2 direction = vec2(cos(angle), sin(angle));\n    \n    vec2 warpUV = uv * scale;\n    \n    if (warp > 0.01) {\n        float warpX = fbm(vec2(warpUV.x + speed * 0.5 * warpSpeed * direction.x, warpUV.y + speed * 0.25 * warpSpeed * direction.y));\n        float warpY = fbm(vec2(warpUV.x + speed * 0.25 * warpSpeed * direction.x + 100.0, warpUV.y + speed * 0.5 * warpSpeed * direction.y + 50.0));\n        warpUV += vec2((warpX - 0.5) * warp, (warpY - 0.5) * warp);\n    }\n    \n    float noiseVal = fbm(warpUV + speed * direction * 0.5);\n    \n    if (u_invert > 0.5) noiseVal = 1.0 - noiseVal;\n    \n    vec3 color = vec3(u_colorR, u_colorG, u_colorB) * noiseVal;\n    vec4 original = texture2D(u_texture, uv);\n    vec4 result = vec4(color, original.a);\n    result = mix(result, original, u_blendOriginal);\n    gl_FragColor = clamp(result, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("evolution", 0.5f, 0.0f, 3.0f, "Fractal");
        addValueWithPropertiesCat("scale", 1.0f, 0.1f, 5.0f, "Fractal");
        addValueWithPropertiesCat("octaves", 4.0f, 1.0f, 8.0f, "Fractal");
        addValueWithPropertiesCat("lacunarity", 2.0f, 1.0f, 4.0f, "Fractal");
        addValueWithPropertiesCat("gain", 0.5f, 0.1f, 1.0f, "Fractal");
        addValueWithPropertiesCat("direction", 0.0f, -1.0f, 1.0f, "Flow");
        addValueWithPropertiesCat("warp", 0.0f, 0.0f, 2.0f, "Warp");
        addValueWithPropertiesCat("warpSpeed", 1.0f, 0.0f, 3.0f, "Warp");
        addValueWithPropertiesCat("colorR", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("colorG", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("colorB", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("invert", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("blendOriginal", 0.0f, 0.0f, 1.0f, "Blend");
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public boolean isShaderEditable() {
        return false;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        super.onApplyCustomization(customPropertiesList);
    }
}

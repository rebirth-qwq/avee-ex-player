package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class DissolveEffectElement extends DummyElement {
    public static final String typeName = "Dissolve";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_dissolve);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_amount;\nuniform float u_evolution;\nuniform float u_scale;\nuniform float u_softness;\nuniform float u_reverse;\nuniform float u_edgeR;\nuniform float u_edgeG;\nuniform float u_edgeB;\nuniform float u_edgeGlow;\nuniform float u_blendOriginal;\n\nfloat hash(vec2 p) {\n    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);\n}\n\nfloat smoothNoise(vec2 p) {\n    vec2 i = floor(p);\n    vec2 f = fract(p);\n    f = f * f * (3.0 - 2.0 * f);\n    float a = hash(i);\n    float b = hash(i + vec2(1.0, 0.0));\n    float c = hash(i + vec2(0.0, 1.0));\n    float d = hash(i + vec2(1.0, 1.0));\n    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);\n}\n\nfloat fbm(vec2 p) {\n    float value = 0.0;\n    float amplitude = 0.5;\n    float frequency = 1.0;\n    for (int i = 0; i < 5; i++) {\n        value += amplitude * smoothNoise(p * frequency);\n        amplitude *= 0.5;\n        frequency *= 2.0;\n    }\n    return value;\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    float time = u_evolution * 3.0;\n    float scale = u_scale * 5.0 + 1.0;\n    float softness = u_softness * 0.05;\n    float noiseVal = fbm(vec2(uv.x * scale, uv.y * scale) + time * 0.5);\n    float threshold = u_amount;\n    if (u_reverse > 0.5) threshold = 1.0 - u_amount;\n    float dissolve = smoothstep(threshold - softness, threshold + softness, noiseVal);\n    float alpha = 1.0 - dissolve;\n    if (u_reverse > 0.5) alpha = dissolve;\n    vec4 color = texture2D(u_texture, uv);\n    float edgeFactor = 1.0 - abs(dissolve - 0.5) * 2.0;\n    float glow = exp(-edgeFactor * 20.0) * u_edgeGlow;\n    vec3 edgeColor = vec3(u_edgeR, u_edgeG, u_edgeB);\n    color.rgb += edgeColor * glow * 0.5;\n    color.a = alpha;\n    vec4 original = texture2D(u_texture, uv);\n    color = mix(color, original, u_blendOriginal * alpha);\n    gl_FragColor = clamp(color, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("amount", 0.5f, 0.0f, 1.0f, typeName);
        addValueWithPropertiesCat("evolution", 0.5f, 0.0f, 3.0f, typeName);
        addValueWithPropertiesCat("scale", 1.0f, 0.1f, 3.0f, typeName);
        addValueWithPropertiesCat("softness", 0.5f, 0.0f, 3.0f, typeName);
        addValueWithPropertiesCat("reverse", 0.0f, 0.0f, 1.0f, typeName);
        addValueWithPropertiesCat("edgeR", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("edgeG", 0.5f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("edgeB", 0.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("edgeGlow", 1.0f, 0.0f, 3.0f, "Color");
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

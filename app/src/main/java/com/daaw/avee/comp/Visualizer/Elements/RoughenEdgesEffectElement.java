package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class RoughenEdgesEffectElement extends DummyElement {
    public static final String typeName = "RoughenEdges";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_roughen_edges);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_edgeWidth;\nuniform float u_roughness;\nuniform float u_frequency;\nuniform float u_warp;\nuniform float u_fray;\nuniform float u_frayEvolution;\nuniform float u_colorEdge;\nuniform float u_blendOriginal;\n\nfloat hash(vec2 p) {\n    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);\n}\n\nfloat smoothNoise(vec2 p) {\n    vec2 i = floor(p);\n    vec2 f = fract(p);\n    f = f * f * (3.0 - 2.0 * f);\n    float a = hash(i);\n    float b = hash(i + vec2(1.0, 0.0));\n    float c = hash(i + vec2(0.0, 1.0));\n    float d = hash(i + vec2(1.0, 1.0));\n    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);\n}\n\nfloat fbm(vec2 p) {\n    float value = 0.0;\n    float amplitude = 0.5;\n    float frequency = 1.0;\n    for (int i = 0; i < 4; i++) {\n        value += amplitude * smoothNoise(p * frequency);\n        amplitude *= 0.5;\n        frequency *= 2.0;\n    }\n    return value;\n}\n\nfloat fbmFreq(vec2 p, float freq) {\n    float value = 0.0;\n    float amplitude = 0.5;\n    float frequency = freq;\n    for (int i = 0; i < 4; i++) {\n        value += amplitude * smoothNoise(p * frequency);\n        amplitude *= 0.5;\n        frequency *= 2.0;\n    }\n    return value;\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec2 edgeDist = min(uv, 1.0 - uv);\n    float distToEdge = min(edgeDist.x, edgeDist.y);\n    float edgeWidth = u_edgeWidth * 0.05;\n    float freq = u_frequency * 2.0 + 0.5;\n    float noiseVal = fbmFreq(uv, freq);\n    float roughness = u_roughness * 0.02;\n    float edgeOffset = (noiseVal - 0.5) * roughness;\n    float edgeThreshold = edgeWidth + edgeOffset;\n    float fray = u_fray * 0.01;\n    float frayNoise = fbmFreq(vec2(uv.x * 100.0 + u_frayEvolution * 2.0, uv.y * 100.0), freq * 0.5);\n    float warp = u_warp * 0.01;\n    float warpX = fbmFreq(vec2(uv.y * 50.0 + u_frayEvolution * 0.5, u_frayEvolution * 0.3), freq * 0.3);\n    float warpY = fbmFreq(vec2(uv.x * 50.0 + u_frayEvolution * 0.5 + 100.0, u_frayEvolution * 0.3 + 50.0), freq * 0.3);\n    vec2 warpOffset = vec2((warpX - 0.5) * warp, (warpY - 0.5) * warp);\n    vec2 distortedUV = uv + warpOffset;\n    vec4 color = texture2D(u_texture, clamp(distortedUV, 0.0, 1.0));\n    float alpha = 1.0;\n    if (distToEdge < edgeThreshold) {\n        float t = distToEdge / max(edgeThreshold, 0.001);\n        alpha = smoothstep(0.0, 1.0, t);\n        alpha = pow(alpha, 0.5 + (1.0 + (frayNoise - 0.5) * u_fray * 0.5) * 0.5);\n        if (distToEdge < edgeThreshold * 0.1) alpha = 0.0;\n    }\n    vec4 edgeColor = vec4(0.0);\n    if (u_colorEdge > 0.5) {\n        edgeColor = texture2D(u_texture, clamp(distortedUV + vec2(0.01, 0.0), 0.0, 1.0));\n    }\n    color = mix(edgeColor, color, alpha);\n    vec4 original = texture2D(u_texture, uv);\n    color = mix(color, original, u_blendOriginal);\n    gl_FragColor = vec4(color.rgb, alpha);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("edgeWidth", 5.0f, 0.0f, 20.0f, "Edge");
        addValueWithPropertiesCat("roughness", 5.0f, 0.0f, 20.0f, "Edge");
        addValueWithPropertiesCat("frequency", 2.0f, 0.1f, 8.0f, "Edge");
        addValueWithPropertiesCat("warp", 2.0f, 0.0f, 10.0f, "Edge");
        addValueWithPropertiesCat("fray", 3.0f, 0.0f, 15.0f, "Fray");
        addValueWithPropertiesCat("frayEvolution", 1.0f, 0.0f, 5.0f, "Fray");
        addValueWithPropertiesCat("colorEdge", 0.0f, 0.0f, 1.0f, "Color");
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

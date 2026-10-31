package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class PixelStretchEffectElement extends DummyElement {
    public static final String typeName = "PixelStretch";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_pixel_stretch);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_strength;\nuniform float u_angle;\nuniform float u_interval;\nuniform float u_threshold;\nuniform float u_evolution;\nuniform float u_jitter;\n\nfloat rand(vec2 p) {\n    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    float res = 1024.0;\n    vec2 pixelUV = floor(uv * res) / res;\n    float angle = u_angle * 6.28318;\n    vec2 dir = vec2(cos(angle), sin(angle));\n    vec2 perp = vec2(-dir.y, dir.x);\n    float mainCoord = dot(pixelUV, dir);\n    float perpCoord = dot(pixelUV, perp);\n    float interval = max(u_interval, 1.0);\n    float mainIndex = floor(mainCoord * res / interval);\n    float perpIndex = floor(perpCoord * res / interval);\n    float seed = mainIndex * 100.0 + perpIndex * 7.0;\n    float random = rand(vec2(seed, floor(u_evolution * 10.0)));\n    float threshold = u_threshold;\n    float stretch = 0.0;\n    if (random > threshold) {\n        stretch = (random - threshold) / (1.0 - threshold) * u_strength;\n    }\n    float jitterAmount = u_jitter * 0.02;\n    float randStart = rand(vec2(seed + 999.0, floor(u_evolution * 3.0)));\n    vec2 startOffset = dir * (randStart - 0.5) * jitterAmount * 2.0;\n    float offset = stretch * 0.5;\n    vec2 stretchOffset = dir * offset;\n    vec2 scatteredUV = uv + startOffset + stretchOffset;\n    scatteredUV = clamp(scatteredUV, 0.0, 1.0);\n    vec4 color = texture2D(u_texture, scatteredUV);\n    gl_FragColor = clamp(color, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("strength", 0.5f, 0.0f, 2.0f, "Stretch");
        addValueWithPropertiesCat("angle", 0.0f, -1.0f, 1.0f, "Stretch");
        addValueWithPropertiesCat("interval", 4.0f, 1.0f, 20.0f, "Stretch");
        addValueWithPropertiesCat("threshold", 0.8f, 0.0f, 1.0f, "Stretch");
        addValueWithPropertiesCat("evolution", 1.0f, 0.0f, 3.0f, "Timing");
        addValueWithPropertiesCat("jitter", 0.0f, 0.0f, 2.0f, "Jitter");
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

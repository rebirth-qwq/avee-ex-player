package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class VectorBlurEffectElement extends DummyElement {
    public static final String typeName = "VectorBlur";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_vector_blur);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_strength;\nuniform float u_samples;\nuniform float u_angle;\nuniform float u_falloff;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_blendOriginal;\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    float angle = u_angle * 6.28318;\n    vec2 dir = vec2(cos(angle), sin(angle));\n    vec2 center = vec2(u_centerX, u_centerY);\n    vec2 delta = uv - center;\n    float dist = length(delta);\n    float falloff = 1.0 - exp(-dist * u_falloff * 10.0);\n    float strength = u_strength * 0.01 * falloff;\n    int samples = int(floor(u_samples));\n    samples = max(samples, 2);\n    samples = min(samples, 64);\n    vec4 color = vec4(0.0);\n    float total = 0.0;\n    for (int i = 0; i < 64; i++) {\n        if (i >= samples) break;\n        float t = float(i) / float(samples - 1);\n        float pos = (t - 0.5) * 2.0;\n        float weight = exp(-pos * pos * 4.0);\n        vec2 offset = dir * pos * strength;\n        vec2 sampleUV = uv + offset;\n        sampleUV = clamp(sampleUV, 0.0, 1.0);\n        color += texture2D(u_texture, sampleUV) * weight;\n        total += weight;\n    }\n    color /= max(total, 0.001);\n    vec4 original = texture2D(u_texture, uv);\n    color = mix(color, original, u_blendOriginal);\n    gl_FragColor = clamp(color, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("strength", 1.0f, 0.0f, 5.0f, "Blur");
        addValueWithPropertiesCat("samples", 16.0f, 4.0f, 64.0f, "Blur");
        addValueWithPropertiesCat("angle", 0.0f, -1.0f, 1.0f, "Blur");
        addValueWithPropertiesCat("falloff", 0.5f, 0.0f, 1.0f, "Blur");
        addValueWithPropertiesCat("centerX", 0.5f, 0.0f, 1.0f, "Center");
        addValueWithPropertiesCat("centerY", 0.5f, 0.0f, 1.0f, "Center");
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

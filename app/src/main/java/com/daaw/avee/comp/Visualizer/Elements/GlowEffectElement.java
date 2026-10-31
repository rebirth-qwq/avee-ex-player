package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class GlowEffectElement extends DummyElement {
    public static final String typeName = "Glow";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_glow);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\nprecision lowp samplerCube;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_strength;\nuniform float u_radius;\nuniform float u_threshold;\nuniform float u_colorR;\nuniform float u_colorG;\nuniform float u_colorB;\nuniform float u_samples;\nuniform float u_blendOriginal;\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    vec4 color = texture2D(u_texture, uv);\n    float luminance = dot(color.rgb, vec3(0.299, 0.587, 0.114));\n    float threshold = u_threshold;\n    float glowFactor = smoothstep(threshold, 1.0, luminance);\n    float radius = u_radius * 0.01;\n    int samples = int(floor(u_samples));\n    samples = max(samples, 2);\n    samples = min(samples, 64);\n    vec4 glowColor = vec4(0.0);\n    float total = 0.0;\n    for (int i = 0; i < 64; i++) {\n        if (i >= samples) break;\n        float angle = 6.28318 * float(i) / float(samples);\n        float rad = float(i) / float(samples) * radius;\n        vec2 offset = vec2(cos(angle), sin(angle)) * rad;\n        vec4 sampleColor = texture2D(u_texture, clamp(uv + offset, 0.0, 1.0));\n        float sampleLum = dot(sampleColor.rgb, vec3(0.299, 0.587, 0.114));\n        float sampleGlow = smoothstep(threshold, 1.0, sampleLum);\n        float weight = 1.0 - float(i) / float(samples);\n        glowColor += sampleColor * sampleGlow * weight;\n        total += weight;\n    }\n    glowColor /= max(total, 0.001);\n    vec3 tint = vec3(u_colorR, u_colorG, u_colorB);\n    glowColor.rgb *= tint * u_strength * 0.5;\n    color.rgb += glowColor.rgb;\n    vec4 original = texture2D(u_texture, uv);\n    color = mix(color, original, u_blendOriginal);\n    gl_FragColor = clamp(color, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("strength", 0.5f, 0.0f, 2.0f, typeName);
        addValueWithPropertiesCat("radius", 5.0f, 0.5f, 20.0f, typeName);
        addValueWithPropertiesCat("threshold", 0.3f, 0.0f, 1.0f, typeName);
        addValueWithPropertiesCat("colorR", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("colorG", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("colorB", 1.0f, 0.0f, 1.0f, "Color");
        addValueWithPropertiesCat("samples", 8.0f, 2.0f, 64.0f, "Quality");
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

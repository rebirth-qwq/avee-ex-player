package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class RadialBlurEffectElement extends DummyElement {
    public static final String typeName = "RadialBlur";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_radial_blur);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision highp int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_samples;\nuniform float u_angle;\nuniform float u_centerX;\nuniform float u_centerY;\nuniform float u_blend;\n\nvec2 rotateUV(vec2 uv, vec2 center, float angleDeg) {\n    float rad = angleDeg * 0.0174533;\n    float c = cos(rad);\n    float s = sin(rad);\n    vec2 p = uv - center;\n    return vec2(p.x * c - p.y * s, p.x * s + p.y * c) + center;\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    int samples = int(u_samples);\n    float maxAngle = u_angle;\n    vec2 center = vec2(u_centerX, u_centerY);\n    float blend = clamp(u_blend, 0.0, 1.0);\n    if (blend < 0.01 || samples < 2) {\n        gl_FragColor = texture2D(u_texture, uv);\n        return;\n    }\n    vec4 accum = vec4(0.0);\n    float totalWeight = 0.0;\n    for (int i = 0; i < 100; i++) {\n        if (i >= samples) break;\n        float t = float(i) / float(samples - 1);\n        float angle = (t - 0.5) * maxAngle * 2.0;\n        vec2 sampleUV = rotateUV(uv, center, angle);\n        sampleUV = clamp(sampleUV, 0.0, 1.0);\n        vec4 col = texture2D(u_texture, sampleUV);\n        float weight = 1.0;\n        accum += col * weight;\n        totalWeight += weight;\n    }\n    accum /= max(totalWeight, 0.001);\n    vec4 original = texture2D(u_texture, uv);\n    gl_FragColor = mix(original, accum, blend);\n}\n";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("samples", 30.0f, 2.0f, 100.0f, "Blur");
        addValueWithPropertiesCat("angle", 90.0f, 0.0f, 360.0f, "Blur");
        addValueWithPropertiesCat("centerX", 0.5f, 0.0f, 1.0f, "Blur");
        addValueWithPropertiesCat("centerY", 0.5f, 0.0f, 1.0f, "Blur");
        addValueWithPropertiesCat("blend", 0.8f, 0.0f, 1.0f, "Blur");
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

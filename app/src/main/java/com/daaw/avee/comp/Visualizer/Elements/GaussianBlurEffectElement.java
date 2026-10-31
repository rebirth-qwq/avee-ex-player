package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.Common.UtilsFileSys;
import com.daaw.avee.MainActivity;
import com.daaw.avee.PlayerCore;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class GaussianBlurEffectElement extends DummyElement {
    public static final String typeName = "GaussianBlur";

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement, com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    protected String getCustomizationDisplayName() {
        return MainActivity.getInstance().getString(R.string.element_title_bar_name_gaussian_blur);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomShader() {
        this.shaderVert = UtilsFileSys.readResource(PlayerCore.s().getAppContext().getResources(), R.raw.buffer_fisheye_vert);
        this.shaderFrag = "precision highp float;\nprecision mediump int;\nprecision lowp sampler2D;\n\nvarying vec2 vTexCoord;\nuniform sampler2D u_texture;\n\nuniform float u_radius;\nuniform float u_radiusX;\nuniform float u_radiusY;\nuniform float u_quality;\nuniform float u_repeatEdge;\nuniform float u_blendOriginal;\n\nvec4 sampleTexture(vec2 uv) {\n    if (u_repeatEdge > 0.5) {\n        return texture2D(u_texture, clamp(uv, 0.0, 1.0));\n    } else {\n        return texture2D(u_texture, uv);\n    }\n}\n\nvoid main() {\n    vec2 uv = vTexCoord;\n    float radiusX = u_radius * u_radiusX * 0.02;\n    float radiusY = u_radius * u_radiusY * 0.02;\n    int samples = int(floor(u_quality * 16.0 + 2.0));\n    samples = max(samples, 2);\n    samples = min(samples, 32);\n    \n    float weights[32];\n    float totalWeight = 0.0;\n    for (int i = 0; i < 32; i++) {\n        if (i >= samples) break;\n        float t = float(i) / float(samples - 1);\n        float pos = (t - 0.5) * 2.0;\n        weights[i] = exp(-pos * pos * 4.0);\n        totalWeight += weights[i];\n    }\n    \n    vec4 color = vec4(0.0);\n    float total = 0.0;\n    for (int i = 0; i < 32; i++) {\n        if (i >= samples) break;\n        float t = float(i) / float(samples - 1);\n        float pos = (t - 0.5) * 2.0;\n        vec2 sampleUV = uv + vec2(pos * radiusX, pos * radiusY);\n        color += sampleTexture(sampleUV) * weights[i];\n        total += weights[i];\n    }\n    color /= max(total, 0.001);\n    \n    vec4 original = texture2D(u_texture, uv);\n    color = mix(color, original, u_blendOriginal);\n    gl_FragColor = clamp(color, 0.0, 1.0);\n}";
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.DummyElement
    public void initCustomValues() {
        addValueWithPropertiesCat("radius", 5.0f, 0.0f, 25.0f, "Blur");
        addValueWithPropertiesCat("radiusX", 1.0f, 0.0f, 5.0f, "Blur");
        addValueWithPropertiesCat("radiusY", 1.0f, 0.0f, 5.0f, "Blur");
        addValueWithPropertiesCat("quality", 0.25f, 0.1f, 1.0f, "Quality");
        addValueWithPropertiesCat("repeatEdge", 1.0f, 0.0f, 1.0f, "Blur");
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
